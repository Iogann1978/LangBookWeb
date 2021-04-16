package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.service.UserService;
import ru.home.langbookweb.service.WordService;

import javax.annotation.security.RolesAllowed;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/dictionary")
@Slf4j
public class DictionaryController {
    private static final int rowsOnPage = 10;
    private static final Sort sorting = Sort.by("word").and(Sort.by("id"));
    @Autowired
    private WordService wordService;
    @Autowired
    private UserService userService;
    private Pageable pageable = PageRequest.of(0, rowsOnPage, sorting);
    private int lastPage = 0;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getDictionary(@RequestParam(defaultValue = "") String findWord, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Flux<? super Word> words = wordService.getPage(findWord, pageable);
        Mono<Long> count = wordService.getCount();
        Flux<Long> pages = count.map(c -> {
            lastPage = (int) Math.ceil((double) c / (double) rowsOnPage);
            return lastPage;
        }).flatMapIterable(lp -> LongStream.rangeClosed(1, lp).boxed().collect(Collectors.toList()));
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        model.addAttribute("pages", pages);
        model.addAttribute("count", count);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/page")
    public String getPage(@RequestParam(defaultValue = "1") int p) {
        pageable = PageRequest.of(p - 1, rowsOnPage, sorting);
        return "redirect:/dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/first")
    public String getFirstPage() {
        pageable = pageable.first();
        return "redirect:/dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/prev")
    public String getPrevPage() {
        pageable = pageable.previousOrFirst();
        return "redirect:/dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/next")
    public String getNextPage() {
        if (pageable.getPageNumber() != lastPage - 1) pageable = pageable.next();
        return "redirect:/dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/last")
    public String getLastPage() {
        if (lastPage != 0) pageable = PageRequest.of(lastPage - 1, rowsOnPage, sorting);
        return "redirect:/dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@ModelAttribute("word") Word word) {
        return "redirect:" + UriComponentsBuilder.fromPath("/dictionary").query("findWord={findWord}")
                .build(word.getWord());
    }
}
