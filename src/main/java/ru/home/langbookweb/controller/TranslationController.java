package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.service.TranslationService;
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
    @GetMapping("/add")
    public String addTranslation(@RequestParam Long wordId, Model model) {
        Mono<? super Word> word = wordService.get(wordId);
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(word.flatMapIterable(w -> ((Word) w).getTranslations()));
        Translation translation = new Translation();
        model.addAttribute("word", word);
        model.addAttribute("translation", translation);
        model.addAttribute("translations", reactiveDataDrivenMode);
        return "translation_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public Mono<Void> saveTranslation(@ModelAttribute("translation") Translation translation, ServerHttpResponse response) {
        Mono<Long> wid = translationService.save(translation);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/example/add").query("translationId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String delTranslation(@ModelAttribute("translation") Translation translation) {
        translationService.del(translation);
        return null;
    }
}
