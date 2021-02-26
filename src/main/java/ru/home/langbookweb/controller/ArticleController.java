package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import ru.home.langbookweb.model.Article;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/article")
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
}
