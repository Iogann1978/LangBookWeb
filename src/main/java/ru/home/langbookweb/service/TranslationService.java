package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.repository.TranslationRepository;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;

    public Mono<Long> save(Translation translation) {
        return Mono.just(translationRepository.saveAndFlush(translation))
                .map(Translation::getId);
    }
}
