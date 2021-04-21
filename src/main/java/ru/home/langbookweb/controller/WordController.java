package ru.home.langbookweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.service.WordService;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.Set;

@Controller
@RequestMapping(value = "/word")
@Slf4j
public class WordController {
    private WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getWord(@RequestParam Long wordId, Model model) {
        Mono<? extends Word> word = wordService.get(wordId);
        model.addAttribute("verb", word);
        model.addAttribute("noun", word);
        model.addAttribute("adjective", word);
        model.addAttribute("adverb", word);
        model.addAttribute("participle", word);
        model.addAttribute("phrase", word);
        model.addAttribute("word", word);
        return "word";
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping("/add")
    public String addWord(@RequestParam(required = false) String fill, Model model) {
        model.addAttribute("word", Word.builder().word(fill).build());
        model.addAttribute("noun", Noun.builder().word(fill).build());
        model.addAttribute("verb", Verb.builder().word(fill).build());
        model.addAttribute("adjective", Adjective.builder().word(fill).build());
        model.addAttribute("adverb", Adverb.builder().word(fill).build());
        model.addAttribute("participle", Participle.builder().word(fill).build());
        model.addAttribute("phrase", new Phrase());
        return "word_add";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/word")
    public Mono<Void> saveWord(@ModelAttribute("word") Word word, ServerHttpResponse response) {
        if (word.getTranslations() == null)
            word.setTranslations(Set.of(Translation.builder().description(word.getTooltip()).source("общий").word(word).build()));
        Mono<Long> wid = wordService.save(word);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/noun")
    public Mono<Void> saveNoun(@ModelAttribute("noun") Noun noun, ServerHttpResponse response) {
        if (noun.getTranslations() == null)
            noun.setTranslations(Set.of(Translation.builder().description(noun.getTooltip()).source("общий").word(noun).build()));
        Mono<Long> wid = wordService.save(noun);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/verb")
    public Mono<Void> saveVerb(@ModelAttribute("verb") Verb verb, ServerHttpResponse response) {
        if (verb.getTranslations() == null)
            verb.setTranslations(Set.of(Translation.builder().description(verb.getTooltip()).source("общий").word(verb).build()));
        Mono<Long> wid = wordService.save(verb);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adjective")
    public Mono<Void> saveAdjective(@ModelAttribute("adjective") Adjective adjective, ServerHttpResponse response) {
        if (adjective.getTranslations() == null)
            adjective.setTranslations(Set.of(Translation.builder().description(adjective.getTooltip()).source("общий").word(adjective).build()));
        Mono<Long> wid = wordService.save(adjective);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/adverb")
    public Mono<Void> saveAdverb(@ModelAttribute("adverb") Adverb adverb, ServerHttpResponse response) {
        if (adverb.getTranslations() == null)
            adverb.setTranslations(Set.of(Translation.builder().description(adverb.getTooltip()).source("общий").word(adverb).build()));
        Mono<Long> wid = wordService.save(adverb);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/participle")
    public Mono<Void> saveParticiple(@ModelAttribute("participle") Participle participle, ServerHttpResponse response) {
        if (participle.getTranslations() == null)
            participle.setTranslations(Set.of(Translation.builder().description(participle.getTooltip()).source("общий").word(participle).build()));
        Mono<Long> wid = wordService.save(participle);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/save/phrase")
    public Mono<Void> savePhrase(@ModelAttribute("phrase") Phrase phrase, ServerHttpResponse response) {
        if (phrase.getTranslations() == null)
            phrase.setTranslations(Set.of(Translation.builder().description(phrase.getTooltip()).source("общий").word(phrase).build()));
        Mono<Long> wid = wordService.save(phrase);
        return wid.flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(UriComponentsBuilder.fromPath("/translation/add").query("wordId={id}").build(id));
            return response.setComplete();
        });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> delWord(@ModelAttribute("word") Word word, ServerHttpResponse response) {
        return wordService.del(word).flatMap(id -> {
            response.setStatusCode(HttpStatus.SEE_OTHER);
            response.getHeaders().setLocation(URI.create("/dictionary"));
            return response.setComplete();
        });
    }
}
