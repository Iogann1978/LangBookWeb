package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.home.langbookweb.model.*;

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
        model.addAttribute("adverb", new Adverb());
        model.addAttribute("participle", new Participle());
        model.addAttribute("phrase", new Phrase());
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save")
    public String saveWord(@RequestParam String type, Model model) {
        switch (type) {
            case "word":
                break;
            case "noun":
                break;
            case "verb":
                break;
            case "adjective":
                break;
            case "adverb":
                break;
            case "participle":
                break;
            case "phrase":
                break;
            default:
                break;
        }
        return "redirect:../translation/add?wordId=1";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String deleteWord(@ModelAttribute("word") Word word) {
        return "dictionary";
    }
}
