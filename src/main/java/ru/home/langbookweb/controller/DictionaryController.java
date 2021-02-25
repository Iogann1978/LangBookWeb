package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import ru.home.langbookweb.model.Noun;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Verb;
import ru.home.langbookweb.model.Word;

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
        Flux<? super Word> words = Flux.just(
                Word.builder().word("in").translations(Set.of(Translation.builder().description("в").build())).build(),
                Noun.builder().word("body").plural("bodies").roundRobin(true).translations(Set.of(Translation.builder().description("тело").build())).build(),
                Verb.builder().word("go").past("went").participle("gone").translations(Set.of(Translation.builder().description("идти").build())).build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(words);
        model.addAttribute("words", reactiveDataDrivenMode);
        model.addAttribute("pages", new int[] {1, 2, 3, 4, 5, 6, 7});
        model.addAttribute("word", new Word());
        return "dictionary";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/find")
    public String findWord(@ModelAttribute("word") Word word) {
        return "dictionary";
    }
}
