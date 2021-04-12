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
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(value = "/text")
@Slf4j
public class TextController {
    private static final int rowsOnPage = 10;
    private Pageable pageable = PageRequest.of(0, rowsOnPage);
    private int lastPage = 0;
    @Autowired
    private TextService textService;
    @Autowired
    private UserService userService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getTexts(Model model) {
        Mono<String> user = userService.getUser().map(u -> u.getUsername());
        Flux<WordItem> pageWords = textService.getPage(pageable);
        Mono<Long> count = textService.getFlux().count().doOnSuccess(c -> lastPage = (int) Math.ceil((double) c / (double) rowsOnPage));
        model.addAttribute("pageWords", pageWords);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        model.addAttribute("user", user);
        model.addAttribute("count", count);
        return "text";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/first")
    public String getFirstPage() {
        pageable = pageable.first();
        return "redirect:/text/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/prev")
    public String getPrevPage() {
        pageable = pageable.previousOrFirst();
        return "redirect:/text/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/next")
    public String getNextPage() {
        pageable = pageable.next();
        return "redirect:/text/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/last")
    public String getLastPage() {
        pageable = PageRequest.of(lastPage - 1, rowsOnPage);
        return "redirect:/text/list";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> textUpload(@RequestPart("upload") Mono<FilePart> part, ServerHttpResponse response) {
        return part.flatMapMany(FilePart::content).collect(ByteArrayOutputStream::new, (baos, dataBuffer) -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        }).map(baos -> {
            String text = baos.toString(StandardCharsets.UTF_8);
            log.debug("text: {}", text);
            return textService.parse(text);
        }).flatMap(count -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(URI.create("/text/list"));
            return response.setComplete();
        });
    }
}
