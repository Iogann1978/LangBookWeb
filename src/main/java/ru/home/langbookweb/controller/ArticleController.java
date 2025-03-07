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
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.service.ArticleService;
import ru.home.langbookweb.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping(value = "/articles")
@Slf4j
public class ArticleController extends AbstractPageController {
    private static final int rowsOnPage = 10;
    private static final Sort sorting = Sort.by("name").and(Sort.by("filename"));
    private ArticleService articleService;
    private UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getArticles(@RequestParam Optional<Integer> page, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<Page<Article>> pageArticles = articleService.getPage(pageable);
        Mono<Integer> lastPage = pageArticles.map(p -> p.getTotalPages());
        addPaging(model, page.orElse(1), lastPage);

        model.addAttribute("user", user);
        model.addAttribute("pageArticles", pageArticles);
        model.addAttribute("article", new Article());
        return "articles";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> saveArticle(@ModelAttribute("article") Article article, @RequestPart("upload") Mono<FilePart> part, ServerHttpResponse response) {
        return part.flatMapMany(FilePart::content).collect(ByteArrayOutputStream::new, (baos, dataBuffer) -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            baos.writeBytes(bytes);
        }).flatMap(baos -> {
            try {
                article.setText(baos.toByteArray());
                baos.close();
            } catch (IOException e) {
                log.error("error reading article file: {}", e.getMessage());
                e.printStackTrace();
            }
            return articleService.save(article);
        }).flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(URI.create("/articles"));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> delArticle(@ModelAttribute("article") Article article, ServerHttpResponse response) {
        return articleService.del(article)
                .flatMap(id -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(URI.create("/articles"));
                    return response.setComplete();
                });
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping(value = "/content", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<ResponseEntity<String>> getArticleContents(@RequestParam Long articleId) {
        Mono<Article> article = articleService.get(articleId);
        return article.map(a -> new String(a.getText(), StandardCharsets.UTF_8)).map((String html) -> ResponseEntity.ok().body(html));
    }
}
