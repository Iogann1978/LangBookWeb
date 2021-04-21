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
import java.net.URI;

@Controller
@RequestMapping(value = "/translation")
public class TranslationController {
    private WordService wordService;
    private TranslationService translationService;

    @Autowired
    public TranslationController(WordService wordService, TranslationService translationService) {
        this.wordService = wordService;
        this.translationService = translationService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addTranslation(@RequestParam Long wordId, Model model) {
        Mono<? extends Word> word = wordService.get(wordId);
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(word.flatMapIterable(w -> w.getTranslations()));
        Mono<Translation> translation = word.map(w -> Translation.builder().word(w).build());
        model.addAttribute("word", word);
        model.addAttribute("translation", translation);
        model.addAttribute("translations", reactiveDataDrivenMode);
        return "translation_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public Mono<Void> saveTranslation(@ModelAttribute("translation") Translation translation, ServerHttpResponse response) {
        Mono<Long> tid = translationService.save(translation);
        return tid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> delTranslation(@ModelAttribute("translation") Translation translation, ServerHttpResponse response) {
        Mono<Long> tid = translationService.del(translation);
        return tid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }
}
