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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/dictionary")
@Slf4j
public class DictionaryController {
    private static final int rowsOnPage = 10;
    private static final int windowLeftRight = 3;
    private static final Sort sorting = Sort.by("word").and(Sort.by("id"));
    @Autowired
    private WordService wordService;
    @Autowired
    private UserService userService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getDictionary(@RequestParam Optional<Integer> page, @RequestParam Optional<String> findWord, Model model) {
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Flux<? super Word> words = wordService.getPage(findWord.orElse(""), pageable);
        Mono<Long> count = wordService.getCount();
        Mono<Integer> lastPage = count.map(c -> (int) Math.ceil((double) c / (double) rowsOnPage));
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
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        model.addAttribute("pages", pages);
        model.addAttribute("count", count);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("lastPage", lastPage);
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@ModelAttribute("word") Word word) {
        return "redirect:" + UriComponentsBuilder.fromPath("/dictionary").query("findWord={findWord}")
                .build(word.getWord());
    }
}
