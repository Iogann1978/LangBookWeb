package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.service.UtilService;
import ru.home.langbookweb.service.WordService;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/dictionary")
public class DictionaryController {
    private static final int rowsOnPage = 10;
    private static final int pagesOnPage = 5;
    @Autowired
    private WordService wordService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getDictionary(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, pagesOnPage, Sort.by("word"));
        List<Long> pages = new ArrayList<>();
        Mono<String> user = UtilService.getUser();
        Flux<? super Word> words = wordService.getWords(user, null, pageable);
        words.count().map(c -> c / rowsOnPage)
            .doOnNext(c -> pages.addAll(LongStream.rangeClosed(1, c).boxed().collect(Collectors.toList())))
            .doFirst(() -> model.addAttribute("pages", pages))
            .subscribeOn(Schedulers.immediate()).subscribe();
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@RequestParam(defaultValue = "0") int page, @ModelAttribute("word") Word word, Model model) {
        Pageable pageable = PageRequest.of(page, pagesOnPage, Sort.by("word"));
        List<Long> pages = new ArrayList<>();
        Mono<String> user = UtilService.getUser();
        Flux<? super Word> words = wordService.getWords(user, word.getWord(), pageable);
        words.count().map(c -> c / rowsOnPage)
                .doOnNext(c -> pages.addAll(LongStream.rangeClosed(1, c).boxed().collect(Collectors.toList())))
                .doFirst(() -> model.addAttribute("pages", pages))
                .subscribeOn(Schedulers.immediate()).subscribe();
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        return "dictionary";
    }
}
