package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public Flux<Example> get(Long translationId) {
        return translationService.get(translationId).flatMapIterable(trs -> trs.getExamples());
    }

    @Transactional
    public Mono<Long> save(Example example) {
        Example e = example.getId() == null ?
                example : exampleRepository.getOne(example.getId());
        if (example.getTranslation() == null) example.setTranslation(e.getTranslation());
        Mono<User> user = userService.get();
        return user.map(u -> exampleRepository.getExampleByUserAndId(u, example.getId()))
                .map(ee -> exampleRepository.saveAndFlush(example))
                .map(ee -> ee.getTranslation().getId());
    }

    @Transactional
    public Mono<Long> del(Example example) {
        /*
        return translationService.get(example.getTranslation().getId()).flatMap(t -> {
            t.getExamples().remove(example);
            return translationService.save(t);
        });
         */
        Mono<User> user = userService.get();
        return user.map(u -> exampleRepository.getExampleByUserAndId(u, example.getId()))
                .map(e -> {
                    exampleRepository.deleteById(e.getId());
                    return e.getTranslation().getId();
                });
    }
}
