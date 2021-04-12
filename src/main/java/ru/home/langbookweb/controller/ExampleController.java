package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
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
    public Mono<Void> saveExample(@ModelAttribute("example") Example example, ServerHttpResponse response) {
        return exampleService.save(example)
                .flatMap(id -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/example/add").query("translationId={id}").build(id));
                    return response.setComplete();
                });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> delExample(@ModelAttribute("example") Example example, ServerHttpResponse response) {
        return exampleService.del(example)
        .flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/example/add").query("translationId={id}").build(id));
            return response.setComplete();
        });
    }
}
