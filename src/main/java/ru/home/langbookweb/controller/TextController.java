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
import org.springframework.web.util.UriComponentsBuilder;
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

@Controller
@RequestMapping(value = "/text")
@Slf4j
public class TextController extends AbstractPageController {
    private static final int rowsOnPage = 10;
    private static final Sort sorting = Sort.by("count").descending().and(Sort.by("word"));
    private TextService textService;
    private UserService userService;

    @Autowired
    public TextController(TextService textService, UserService userService) {
        this.textService = textService;
        this.userService = userService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getTexts(@RequestParam Optional<Integer> page, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<Page<WordItem>> pageWords = textService.getPage(pageable);
        Mono<Long> count = textService.getCount();
        Mono<Integer> lastPage = count.map(c -> (int) Math.ceil((double) c / (double) rowsOnPage));
        addPaging(model, page.orElse(1), lastPage);

        model.addAttribute("wordItem", new WordItem());
        model.addAttribute("pageWords", pageWords);
        model.addAttribute("user", user);
        model.addAttribute("count", count);
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

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> delWordItem(@RequestParam int page, @ModelAttribute("wordItem") WordItem wordItem, ServerHttpResponse response) {
        return textService.del(wordItem)
                .flatMap(word -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/text").query("page={page}").build(page));
                    return response.setComplete();
                });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/add")
    public Mono<Void> addWord(@ModelAttribute("wordItem") WordItem wordItem, ServerHttpResponse response) {
        return textService.del(wordItem)
                .flatMap(word -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/word/add").query("fill={word}").build(word));
                    return response.setComplete();
                });
    }
}
