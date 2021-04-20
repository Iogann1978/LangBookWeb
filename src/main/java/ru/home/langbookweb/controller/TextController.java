package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.WordItem;
import ru.home.langbookweb.service.TextService;
import ru.home.langbookweb.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/text")
@Slf4j
public class TextController {
    private static final int rowsOnPage = 10;
    private static final int windowLeftRight = 3;
    @Autowired
    private TextService textService;
    @Autowired
    private UserService userService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getTexts(@RequestParam Optional<Integer> page, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage);
        Flux<WordItem> pageWords = textService.getPage(pageable);
        Mono<Long> count = textService.getFlux().count();
        Mono<Integer> lastPage = count.map(c -> (int) Math.ceil((double) c / (double) rowsOnPage));
        Flux<Long> pages = lastPage.flatMapIterable(lp -> {
            int currentPage = page.orElse(1);
            int startPage = currentPage;
            int limit = windowLeftRight * 2 + 1;
            if (currentPage - windowLeftRight >= 1 && currentPage + windowLeftRight <= lp) {
                startPage = currentPage - windowLeftRight;
            } else if (currentPage - windowLeftRight < 1) {
                startPage = 1;
            } else if (currentPage + windowLeftRight > lp) {
                startPage = lp - limit + 1;
            }
            return LongStream.rangeClosed(startPage, lp).limit(limit).boxed().collect(Collectors.toList());
        });
        model.addAttribute("pageWords", pageWords);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        model.addAttribute("pages", pages);
        model.addAttribute("user", user);
        model.addAttribute("count", count);
        model.addAttribute("lastPage", lastPage);
        return "text";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> textUpload(@RequestPart("upload") Mono<FilePart> part, ServerHttpResponse response) {
        return part.flatMap(fp -> fp.content().collect(ByteArrayOutputStream::new, (baos, dataBuffer) -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        }).flatMap(baos -> {
            String content = "";
            try {
                String text = baos.toString(StandardCharsets.UTF_8);
                content = fp.filename().endsWith(".pdf") ?
                        textService.textFromPdf(baos.toByteArray()) : text;
                baos.close();
            } catch (IOException e) {
                log.error("error reading text(pdf) file: {}", e.getMessage());
                e.printStackTrace();
            }
            return textService.parse(content);
        })).flatMap(count -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(URI.create("/text"));
            return response.setComplete();
        });
    }
}
