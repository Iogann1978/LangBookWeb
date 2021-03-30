package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.service.ArticleService;
import ru.home.langbookweb.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/article")
@Slf4j
public class ArticleController {
    private static final int rowsOnPage = 10;
    private Pageable pageable = PageRequest.of(0, rowsOnPage, Sort.by("name").and(Sort.by("filename")));
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    private int lastPage = 0;

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getArticles(Model model) {
        Mono<String> user = userService.getUser().map(u -> u.getLogin());
        Mono<Page<Article>> pageArticles = articleService.getArticles(pageable);
        Flux<Long> pages = pageArticles.flatMapIterable(p -> {
                    lastPage = p.getTotalPages();
                    return LongStream.rangeClosed(1, p.getTotalPages()).boxed().collect(Collectors.toList());
                }
        );
        model.addAttribute("user", user);
        model.addAttribute("pages", pages);
        model.addAttribute("pageArticles", pageArticles);
        model.addAttribute("article", new Article());
        return "articles";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> saveArticle(@ModelAttribute("article") Article article, @RequestPart("upload") Mono<FilePart> part, ServerHttpResponse response) {
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return part.flatMapMany(FilePart::content).collect(ByteArrayOutputStream::new, (baos, dataBuffer) -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        }).flatMap(baos -> {
            log.info("text: {} {}", article.getFilename(), new String(baos.toByteArray(), StandardCharsets.UTF_8));
            article.setText(baos.toByteArray());
            return articleService.save(article);
        }).flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(URI.create("/article/list"));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String deleteArticle(@ModelAttribute("article") Article article) {
        articleService.del(article);
        return "articles";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/page")
    public String getPage(@RequestParam(defaultValue = "1") int p) {
        pageable = PageRequest.of(p - 1, rowsOnPage, Sort.by("name").and(Sort.by("filename")));
        return "redirect:/article/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/first")
    public String getFirstPage() {
        pageable = pageable.first();
        return "redirect:/article/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/prev")
    public String getPrevPage() {
        pageable = pageable.previousOrFirst();
        return "redirect:/article/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/next")
    public String getNextPage() {
        pageable = pageable.next();
        return "redirect:/article/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/last")
    public String getLastPage() {
        pageable = PageRequest.of(lastPage - 1, rowsOnPage, Sort.by("word"));;
        return "redirect:/article/list";
    }
}
