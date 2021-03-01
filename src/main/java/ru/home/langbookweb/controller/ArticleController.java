package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.Article;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(value = "/article")
@Slf4j
public class ArticleController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getArticles(Model model) {
        Flux<Article> articles = Flux.just(
                Article.builder().id(1L).name("Статья 1").filename("article1.html").build(),
                Article.builder().id(2L).name("Статья 2").filename("article2.html").build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(articles);
        model.addAttribute("articles", reactiveDataDrivenMode);
        model.addAttribute("article", new Article());
        model.addAttribute("pages", new int[] {1, 2, 3, 4, 5, 6, 7});
        return "articles";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveArticle(@ModelAttribute("article") Article article, @RequestPart("upload") Mono<FilePart> part) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        part.flatMapMany(FilePart::content).doOnNext(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        })
        .subscribeOn(Schedulers.immediate()).subscribe();
        article.setText(baos.toByteArray());
        log.debug("text: {} {}", article.getFilename(), new String(article.getText(), StandardCharsets.UTF_8));
        return "articles";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String deleteArticle(@ModelAttribute("article") Article article) {
        return "articles";
    }
}
