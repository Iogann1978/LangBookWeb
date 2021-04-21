package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.TranslationRepository;

@Service
@Slf4j
public class TranslationService {
    private TranslationRepository translationRepository;
    private WordService wordService;
    private UserService userService;

    @Autowired
    public TranslationService(TranslationRepository translationRepository, WordService wordService, UserService userService) {
        this.translationRepository = translationRepository;
        this.wordService = wordService;
        this.userService = userService;
    }

    @Transactional
    public Mono<Long> save(Translation translation) {
        Mono<User> user = userService.get();
        Translation t = translation.getId() == null ?
                translation : translationRepository.getTranslationById(translation.getId());
        return wordService.get(t.getWord().getId()).map(w -> {
                t.setWord(w);
                return t;
            }).filterWhen(tr -> user.map(u -> u.getUsername().equals(tr.getWord().getUser().getUsername())))
            .map(tr -> translationRepository.saveAndFlush(translation))
            .map(tr -> tr.getWord().getId());
    }

    @Transactional
    public Mono<Long> del(Translation translation) {
        Translation t = translationRepository.getOne(translation.getId());
        return wordService.get(t.getWord().getId()).map(w -> {
            t.setWord(w);
            return t;
        }).map(tr -> {
            translationRepository.deleteById(tr.getId());
            return tr.getWord().getId();
        });
    }

    @Transactional(readOnly = true)
    public Mono<Translation> get(Long id) {
        Mono<User> user = userService.get();
        return user.map(u -> translationRepository.getTranslationByUserAndId(u, id));
    }
}
