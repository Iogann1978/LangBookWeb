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

@Controller
@RequestMapping(value = "/dictionary")
@Slf4j
public class DictionaryController extends AbstractPageController {
    private static final int rowsOnPage = 10;
    private static final Sort sorting = Sort.by("word").and(Sort.by("id"));
    private WordService wordService;
    private UserService userService;

    @Autowired
    public DictionaryController(WordService wordService, UserService userService) {
        this.wordService = wordService;
        this.userService = userService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getDictionary(@RequestParam Optional<Integer> page, @RequestParam Optional<String> findWord, Model model) {
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Flux<Word> words = wordService.getPage(findWord.orElse(""), pageable);
        Mono<Long> count = wordService.getCount();
        Mono<Integer> lastPage = count.map(c -> (int) Math.ceil((double) c / (double) rowsOnPage));
        addPaging(model, page.orElse(1), lastPage);

        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        model.addAttribute("count", count);
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@ModelAttribute("word") Word word) {
        return "redirect:" + UriComponentsBuilder.fromPath("/dictionary").query("findWord={findWord}")
                .build(word.getWord());
    }
}
