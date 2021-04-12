package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public Mono<Page<Phrase>> getPage(Pageable pageable) {
        Mono<User> user = userService.get();
        Mono<Page<Phrase>> page = user.map(u -> phraseRepository.getAllByUser(u, pageable));
        return page;
    }
}
