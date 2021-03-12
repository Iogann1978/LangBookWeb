package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.Article;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/text")
@Slf4j
public class TextController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getTexts(Model model) {
        List<String> words = new ArrayList<>();
        model.addAttribute("words", words);
        model.addAttribute("pages", new int[] {1, 2, 3, 4, 5, 6, 7});
        model.addAttribute("filename", new String());
        return "text";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String textUpload(@RequestPart("upload") Mono<FilePart> part) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        part.flatMapMany(FilePart::content).doOnNext(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        }).subscribeOn(Schedulers.immediate()).subscribe();
        log.debug("text: {}", baos.toString(StandardCharsets.UTF_8));
        return "text";
    }
}
