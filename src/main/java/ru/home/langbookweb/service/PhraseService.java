package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.PhraseRepository;

@Service
public class PhraseService {
    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private UserService userService;

    public Mono<Page<Phrase>> getPhrases(Pageable pageable) {
        Mono<User> user = userService.getUser();
        Mono<Page<Phrase>> page = user.map(u -> phraseRepository.getAllByUser(u, pageable));
        return page;
    }
}
