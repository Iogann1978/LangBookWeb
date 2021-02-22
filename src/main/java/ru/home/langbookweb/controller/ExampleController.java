package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import ru.home.langbookweb.model.Example;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/example")
public class ExampleController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addExample(@RequestParam Long translationId, Model model) {
        Word word = Word.builder().word("Body").build();
        Translation translation = Translation.builder().description("Перевод 1").source("Oxford dictionary").build();
        Example example = new Example();
        Flux<Example> examples = Flux.just(
                Example.builder().text1("This is first example").text2("Первый пример").build(),
                Example.builder().text1("This is second example").text2("Второй пример").build()
        );
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(examples, 1);
        model.addAttribute("word", word);
        model.addAttribute("translation", translation);
        model.addAttribute("example", example);
        model.addAttribute("examples", reactiveDataDrivenMode);
        return "example_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public String saveExample(@ModelAttribute("example") Example example) {
        return "redirect:add?translationId=1";
    }
}
