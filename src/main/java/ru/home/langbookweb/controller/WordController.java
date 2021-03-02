package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.home.langbookweb.model.*;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@Controller
@RequestMapping(value = "/word")
public class WordController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getWord(@RequestParam Long wordId, Model model) {
        Example example1 = Example.builder().id(7L).text1("Text 1").text2("Текст 1").build();
        Example example2 = Example.builder().id(6L).text1("Text 2").text2("Текст 2").build();
        Example example3 = Example.builder().id(5L).text1("Text 3").text2("Текст 3").build();
        Example example4 = Example.builder().id(4L).text1("Text 4").text2("Текст 4").build();
        Translation translation1 = Translation.builder().id(3L).description("Перевод 1").source("Oxford dictionary").examples(Set.of(example1, example2)).build();
        Translation translation2 = Translation.builder().id(2L).description("Перевод 2").source("Cambridge dictionary").examples(Set.of(example3, example4)).build();
        Word word = Word.builder().id(wordId).word("Exercise").translations(Set.of(translation1, translation2)).build();
        model.addAttribute("word", word);
        return "word";
    }

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
