package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.TranslationRepository;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;

    @Transactional
    public Mono<Long> save(Translation translation) {
        return Mono.just(translationRepository.saveAndFlush(translation))
                .map(Translation::getId);
    }

    @Transactional
    public void del(Translation translation) {
        translationRepository.delete(translation);
    }
}
