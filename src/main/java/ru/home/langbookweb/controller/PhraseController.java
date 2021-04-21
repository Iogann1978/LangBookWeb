package ru.home.langbookweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.service.PhraseService;
import ru.home.langbookweb.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@Controller
@RequestMapping(value = "/phrases")
public class PhraseController extends AbstractPageController {
    private static final int rowsOnPage = 10;
    private static final Sort sorting = Sort.by("word");
    private PhraseService phraseService;
    private UserService userService;

    @Autowired
    public PhraseController(PhraseService phraseService, UserService userService) {
        this.phraseService = phraseService;
        this.userService = userService;
    }

    @RolesAllowed("USER,ADMIN")
    @GetMapping
    public String getArticles(@RequestParam Optional<Integer> page, Model model) {
        Mono<String> user = userService.get().map(u -> u.getUsername());
        Pageable pageable = PageRequest.of(page.map(p -> p - 1).orElse(0), rowsOnPage, sorting);
        Mono<Page<Phrase>> pagePhrases = phraseService.getPage(pageable);
        Mono<Integer> lastPage = pagePhrases.map(p -> p.getTotalPages());
        addPaging(model, page.orElse(1), lastPage);

        model.addAttribute("user", user);
        model.addAttribute("pagePhrases", pagePhrases);
        return "phrases";
    }
}
