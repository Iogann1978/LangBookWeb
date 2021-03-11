package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.home.langbookweb.model.RoundRobin;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@Controller
@RequestMapping(value = "/roundrobin")
public class RoundRobinController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getRoundRobin(Model model) {
        Set<Translation> trans1 = Set.of(Translation.builder().id(5L).description("Слово").build());
        Set<Translation> trans2 = Set.of(Translation.builder().id(6L).description("Переводить").build(),
                Translation.builder().id(7L).description("Переводить туда-сюда").build());
        Set<Translation> trans3 = Set.of(Translation.builder().id(8L).description("Быстро").build());
        Set<Word> words = Set.of(
                Word.builder().id(2L).word("word").translations(trans1).build(),
                Word.builder().id(3L).word("translate").translations(trans2).build(),
                Word.builder().id(4L).word("quick").translations(trans3).build()
        );
        RoundRobin roundRobin = RoundRobin.builder().id(1L).name("Первая карусель").words(words).build();
        model.addAttribute("roundrobin", roundRobin);
        model.addAttribute("numbers", new int[]{1, 2, 3});
        return "roundrobin";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/add")
    public String roundrobinAdd(Word word) {
        return null;
    }
}
