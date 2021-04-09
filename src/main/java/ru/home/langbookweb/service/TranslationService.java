package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.TranslationRepository;

import java.util.Optional;

@Service
@Slf4j
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public Mono<Long> save(Translation translation) {
        Mono<User> user = userService.getUser();
        Translation t = translation.getId() == null ?
                translation : translationRepository.getOne(translation.getId());
        if (translation.getWord() == null) translation.setWord(t.getWord());
        return user.filter(u -> u.getLogin().equals(t.getWord().getUser().getLogin()))
                    .map(u -> translationRepository.saveAndFlush(translation))
                    .map(Translation::getId);
    }

    @Transactional
    public Mono<Long> del(Translation translation) {
        Mono<User> user = userService.getUser();
        return user.filter(u -> u.getLogin().equals(translation.getWord().getUser().getLogin()))
                .map(u -> {
                    translationRepository.delete(translation);
                    return translation.getWord().getId();
                });
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
