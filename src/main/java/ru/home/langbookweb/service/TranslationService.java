package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.TranslationRepository;

import java.util.Optional;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public Mono<Long> save(Translation translation) {
        return Mono.just(translationRepository.saveAndFlush(translation))
                .map(Translation::getId);
    }

    @Transactional
    public void del(Translation translation) {
        translationRepository.delete(translation);
    }

    @Transactional(readOnly = true)
    public Mono<Translation> get(Long id) {
        Mono<User> user = userService.getUser();
        Optional<Translation> translation = translationRepository.findById(id);
        return user.filter(u1 -> !translation.map(t -> t.getWord())
                .map(w -> w.getUser()).filter(u2 -> u1.getLogin().equals(u2.getLogin())).isEmpty())
                .map(u -> translation.orElse(null));
    }
}
