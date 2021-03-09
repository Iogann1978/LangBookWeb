package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.home.langbookweb.model.RoundRobin;
import ru.home.langbookweb.model.Word;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@Controller
@RequestMapping(value = "/roundrobin")
public class RoundRobinController {

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getRoundRobin(Model model) {
        Set<Word> words = Set.of(
                Word.builder().id(2L).word("word").build(),
                Word.builder().id(3L).word("translate").build(),
                Word.builder().id(4L).word("quick").build()
        );
        RoundRobin roundRobin = RoundRobin.builder().id(1L).name("Первая карусель").words(words).build();
        model.addAttribute("roundrobin", roundRobin);
        model.addAttribute("numbers", new int[]{1, 2, 3});
        return "roundrobin";
    }
}
