package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/translation")
public class TranslationController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addTranslation(@RequestParam Long wordId, Model model) {
        Word word = Word.builder().id(wordId).word("Body").build();
        Flux<Translation> translations = Flux.just(
                Translation.builder().id(1L).description("Перевод 1").source("Oxford dictionary").build(),
                Translation.builder().id(2L).description("Перевод 2").source("Cambridge dictionary").build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(translations);
        Translation translation = new Translation();
        model.addAttribute("word", word);
        model.addAttribute("translation", translation);
        model.addAttribute("translations", reactiveDataDrivenMode);
        return "translation_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public String saveTranslation(@ModelAttribute("translation") Translation translation) {
        return "redirect:add?wordId=1";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String deleteTranslation(@ModelAttribute("translation") Translation translation) {
        return "redirect:add?wordId=1";
    }
}
