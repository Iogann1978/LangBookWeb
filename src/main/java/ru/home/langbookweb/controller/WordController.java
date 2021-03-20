package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Mono<Word> word = wordService.getWord(user, wordId);
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
    public Mono<ResponseEntity<Void>> saveWord(@ModelAttribute("word") Word word) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.saveWord(user, word);
        return wid.map(id -> ResponseEntity.ok().header("Location", "https://localhost:8443/dictionary").<Void>build());
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/noun")
    public Mono<Void> saveNoun(@ModelAttribute("noun") Noun noun, ServerHttpResponse response) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = wordService.saveWord(user, noun);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/verb")
    public String saveVerb(@ModelAttribute("verb") Verb verb) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = null;
        wid = wordService.saveWord(user, verb);
        /*
        wid.map(id -> String.format("redirect:../translation/add?wordId=%d", id))
                .doOnNext(id -> log.info("id: {}", id))
                .subscribeOn(Schedulers.immediate()).subscribe();
        Thread.sleep(1000L);
         */
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adjective")
    public String saveAdjective(@ModelAttribute("adjective") Adjective adjective) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = null;
        wid = wordService.saveWord(user, adjective);
        /*
        wid.map(id -> String.format("redirect:../translation/add?wordId=%d", id))
                .doOnNext(id -> log.info("id: {}", id))
                .subscribeOn(Schedulers.immediate()).subscribe();
        Thread.sleep(1000L);
         */
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adverb")
    public String saveAdverb(@ModelAttribute("adverb") Adverb adverb) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = null;
        wid = wordService.saveWord(user, adverb);
        /*
        wid.map(id -> String.format("redirect:../translation/add?wordId=%d", id))
                .doOnNext(id -> log.info("id: {}", id))
                .subscribeOn(Schedulers.immediate()).subscribe();
        Thread.sleep(1000L);
         */
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/participle")
    public String saveParticiple(@ModelAttribute("participle") Participle participle) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = null;
        wid = wordService.saveWord(user, participle);
        /*
        wid.map(id -> String.format("redirect:../translation/add?wordId=%d", id))
                .doOnNext(id -> log.info("id: {}", id))
                .subscribeOn(Schedulers.immediate()).subscribe();
        Thread.sleep(1000L);
         */
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/phrase")
    public String savePhrase(@ModelAttribute("phrase") Phrase phrase) {
        Mono<String> user = UtilService.getUser();
        Mono<Long> wid = null;
        wid = wordService.saveWord(user, phrase);
        /*
        wid.map(id -> String.format("redirect:../translation/add?wordId=%d", id))
                .doOnNext(id -> log.info("id: {}", id))
                .subscribeOn(Schedulers.immediate()).subscribe();
        Thread.sleep(1000L);
         */
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public String delWord(@ModelAttribute("word") Word word) {
        Mono<String> user = UtilService.getUser();
        wordService.delWord(user, word);
        return "dictionary";
    }
}
