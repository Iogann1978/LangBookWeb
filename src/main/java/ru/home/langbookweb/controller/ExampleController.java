package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Example;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.service.ExampleService;
import ru.home.langbookweb.service.TranslationService;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/example")
@Slf4j
public class ExampleController {
    @Autowired
    private ExampleService exampleService;
    @Autowired
    private TranslationService translationService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addExample(@RequestParam Long translationId, Model model) {
        Mono<Translation> translation = translationService.get(translationId);
        Mono<Word> word = translation.map(t -> t.getWord());
        Mono<Example> example = translation.map(t -> Example.builder().translation(t).build());
        Flux<Example> examples = exampleService.getExamples(translationId);
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(examples);
        model.addAttribute("word", word);
        model.addAttribute("example", example);
        model.addAttribute("translation", translation);
        model.addAttribute("examples", reactiveDataDrivenMode);
        return "example_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public String saveExample(@ModelAttribute("example") Example example) {
        Example e = exampleService.save(example);
        return "redirect:add?translationId=" + e.getTranslation().getId();
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String deleteExample(@ModelAttribute("example") Example example) {
        return "redirect:add?wordId=1";
    }
}
