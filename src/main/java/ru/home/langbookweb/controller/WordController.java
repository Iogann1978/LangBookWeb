package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.service.UtilService;
import ru.home.langbookweb.service.WordService;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@Controller
@RequestMapping(value = "/word")
@Slf4j
public class WordController {
    @Autowired
    private WordService wordService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getWord(@RequestParam Long wordId, Model model) {
        Mono<String> user = UtilService.getUser();
        Mono<Word> word = wordService.get(user, wordId);
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
    @PostMapping("/save/word")
    public Mono<Void> saveWord(@ModelAttribute("word") Word word, ServerHttpResponse response) {
        if (word.getTranslations() == null) word.setTranslations(Set.of(Translation.builder().description("Перевод").build()));
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, word);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/noun")
    public Mono<Void> saveNoun(@ModelAttribute("noun") Noun noun, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, noun);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/verb")
    public Mono<Void> saveVerb(@ModelAttribute("verb") Verb verb, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, verb);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adjective")
    public Mono<Void> saveAdjective(@ModelAttribute("adjective") Adjective adjective, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, adjective);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adverb")
    public Mono<Void> saveAdverb(@ModelAttribute("adverb") Adverb adverb, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, adverb);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/participle")
    public Mono<Void> saveParticiple(@ModelAttribute("participle") Participle participle, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, participle);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/phrase")
    public Mono<Void> savePhrase(@ModelAttribute("phrase") Phrase phrase, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.save(user, phrase);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String delWord(@ModelAttribute("word") Word word) {
        Mono<String> user = UtilService.getUser();
        wordService.del(user, word);
        return "dictionary";
    }
}
