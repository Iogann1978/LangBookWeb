package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.home.langbookweb.model.Adjective;
import ru.home.langbookweb.model.Noun;
import ru.home.langbookweb.model.Verb;
import ru.home.langbookweb.model.Word;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/word")
public class WordController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addWord(Model model) {
        model.addAttribute("word", new Word());
        model.addAttribute("noun", new Noun());
        model.addAttribute("verb", new Verb());
        model.addAttribute("adjective", new Adjective());
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public String saveWord(@ModelAttribute("word") Word word) {
        if (word instanceof Noun) {

        } else if (word instanceof Verb) {

        } else if (word instanceof Adjective) {

        } else {

        }
        return "add_translation";
    }
}
