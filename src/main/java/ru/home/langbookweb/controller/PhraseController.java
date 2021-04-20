package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.service.PhraseService;
import ru.home.langbookweb.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/phrases")
public class PhraseController {
    private static final int rowsOnPage = 10;
    private static final int windowLeftRight = 3;
    private static final Sort sorting = Sort.by("word");
    @Autowired
    private PhraseService phraseService;
    @Autowired
    private UserService userService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getArticles(@RequestParam Optional<Integer> page, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<Page<Phrase>> pagePhrases = phraseService.getPage(pageable);
        Mono<Integer> lastPage = pagePhrases.map(p -> p.getTotalPages());
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
        model.addAttribute("user", user);
        model.addAttribute("pages", pages);
        model.addAttribute("pagePhrases", pagePhrases);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("lastPage", lastPage);
        return "phrases";
    }
}
