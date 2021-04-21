package ru.home.langbookweb.controller;

import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Article;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

public abstract class AbstractPageController {
    private static final int windowLeftRight = 3;

    protected void addPaging(Model model, int page, Mono<Integer> lastPage) {
        Flux<Long> pages = lastPage.flatMapIterable(lp -> {
            int currentPage = page;
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
        model.addAttribute("pages", pages);
        model.addAttribute("article", new Article());
        model.addAttribute("page", page);
        model.addAttribute("lastPage", lastPage);
    }
}
