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
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/phrase")
public class PhraseController {
    private static final int rowsOnPage = 10;
    private Pageable pageable = PageRequest.of(0, rowsOnPage, Sort.by("word"));
    @Autowired
    private PhraseService phraseService;
    @Autowired
    private UserService userService;
    private int lastPage = 0;

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getArticles(Model model) {
        Mono<String> user = userService.getUser().map(u -> u.getLogin());
        Mono<Page<Phrase>> pagePhrases = phraseService.getPhrases(pageable);
        Flux<Long> pages = pagePhrases.flatMapIterable(p -> {
            lastPage = p.getTotalPages();
            return LongStream.rangeClosed(1, p.getTotalPages()).boxed().collect(Collectors.toList());
        }
        );
        model.addAttribute("user", user);
        model.addAttribute("pages", pages);
        model.addAttribute("pagePhrases", pagePhrases);
        return "phrases";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/page")
    public String getPage(@RequestParam(defaultValue = "1") int p) {
        pageable = PageRequest.of(p - 1, rowsOnPage, Sort.by("word"));
        return "redirect:/phrase/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/first")
    public String getFirstPage() {
        pageable = pageable.first();
        return "redirect:/phrase/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/prev")
    public String getPrevPage() {
        pageable = pageable.previousOrFirst();
        return "redirect:/phrase/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/next")
    public String getNextPage() {
        pageable = pageable.next();
        return "redirect:/phrase/list";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/last")
    public String getLastPage() {
        pageable = PageRequest.of(lastPage - 1, rowsOnPage, Sort.by("word"));;
        return "redirect:/phrase/list";
    }
}
