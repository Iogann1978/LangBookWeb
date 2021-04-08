package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Example;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.ExampleRepository;

@Service
public class ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;
    @Autowired
    private TranslationService translationService;

    public Flux<Example> getExamples(Long translationId) {
        return translationService.get(translationId).flatMapIterable(trs -> trs.getExamples());
    }

    public Example save(Example example) {
        return exampleRepository.saveAndFlush(example);
    }
}
