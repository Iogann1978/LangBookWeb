package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import ru.home.langbookweb.model.Phrase;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/phrase")
public class PhraseController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/list")
    public String getArticles(Model model) {
        Flux<Phrase> phrases = Flux.just(
                Phrase.builder().id(1L).word("A little bit").build(),
                Phrase.builder().id(2L).word("To each other").build(),
                Phrase.builder().id(3L).word("A little bit").build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(phrases);
        model.addAttribute("phrases", phrases);
        model.addAttribute("pages", new int[] {1, 2, 3, 4, 5, 6, 7});
        return "phrases";
    }
}
