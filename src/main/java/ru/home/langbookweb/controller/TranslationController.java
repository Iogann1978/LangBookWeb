package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.service.TranslationService;
import ru.home.langbookweb.service.UtilService;
import ru.home.langbookweb.service.WordService;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/translation")
public class TranslationController {
    @Autowired
    private WordService wordService;
    @Autowired
    private TranslationService translationService;

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/add")
    public String addTranslation(@RequestParam Long wordId, Model model) {
        Mono<String> user = UtilService.getUser();
        Mono<Word> word = wordService.getWord(user, wordId);
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(word.flatMapIterable(Word::getTranslations));
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
