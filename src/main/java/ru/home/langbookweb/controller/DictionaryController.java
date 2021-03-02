package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.*;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@Controller
@RequestMapping(value = "/dictionary")
public class DictionaryController {
    private static final int rowsOnPage = 10;
    private int currentPage = 1;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getDicrionary(@RequestParam(defaultValue = "0") int page, Model model) {
        Mono<String> user = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(principal -> ((UserDetails) principal).getUsername());
        Flux<? super Word> words = Flux.just(
                Word.builder().id(1L).word("in").build(),
                Noun.builder().id(2L).word("body").plural("bodies").build(),
                Verb.builder().id(3L).word("go").past("went").participle("gone").build(),
                Adjective.builder().id(4L).word("cool").comparative("cooler").superative("coolest").build(),
                Adverb.builder().id(5L).word("fast").build(),
                Participle.builder().word("telling").build(),
                Phrase.builder().word("my first phrase").build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode = new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("pages", new int[] {1, 2, 3, 4, 5, 6, 7});
        model.addAttribute("word", new Word());
        model.addAttribute("user", user);
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@ModelAttribute("word") Word word) {
        return "dictionary";
    }
}
