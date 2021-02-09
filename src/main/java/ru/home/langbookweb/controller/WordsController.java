package ru.home.langbookweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "/")
public class WordsController {
    @RolesAllowed("USER,ADMIN")
    @GetMapping("/words")
    public String getWords() {
        return "words.html";
    }
}
