package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.RoundRobin;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.service.RoundRobinService;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/roundrobin")
public class RoundRobinController {
    @Autowired
    private RoundRobinService roundRobinService;

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getRoundRobin(Model model) {
        Mono<RoundRobin> roundRobin = roundRobinService.get();
        Flux<Word> words = roundRobin.flatMapIterable(rr -> {
            List<Word> list = rr.getWords().stream().collect(Collectors.toList());
            Collections.shuffle(list);
            return list;
        });
        Flux<Integer> numbers = words.count().flatMapMany(c -> Flux.range(1, c.intValue()));
        model.addAttribute("roundrobin", roundRobin);
        model.addAttribute("numbers", numbers);
        model.addAttribute("word", new Word());
        model.addAttribute("words", words);
        return "roundrobin";
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/add")
    public Mono<Void> roundrobinAdd(@ModelAttribute("word") Word word, ServerHttpResponse response) {
        return roundRobinService.addWord(word)
                .flatMap(id -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(URI.create("/dictionary"));
                    return response.setComplete();
                });
    }

    @RolesAllowed("USER,ADMIN")
    @PostMapping("/del")
    public Mono<Void> roundrobinDel(@ModelAttribute("word") Word word, ServerHttpResponse response) {
        return roundRobinService.delWord(word)
                .flatMap(id -> {
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().setLocation(URI.create("/dictionary"));
                    return response.setComplete();
                });
    }
}
