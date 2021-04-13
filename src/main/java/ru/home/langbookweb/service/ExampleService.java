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
                example : exampleRepository.getExampleById(example.getId());
        if (example.getTranslation() == null) example.setTranslation(e.getTranslation());
        Mono<User> user = userService.get();
        return translationService.get(example.getTranslation().getId()).map(t -> {
            e.setTranslation(t);
            return t;
        }).filterWhen(tr -> user.map(u -> u.getUsername().equals(tr.getWord().getUser().getUsername())))
        .map(tr -> {
            exampleRepository.saveAndFlush(example);
            return e.getTranslation().getId();
        });
    }

    @Transactional
    public Mono<Long> del(Example example) {
        Mono<User> user = userService.get();
        return translationService.get(example.getTranslation().getId()).map(t -> {
            example.setTranslation(t);
            return t;
        }).filterWhen(tr -> user.map(u -> u.getUsername().equals(tr.getWord().getUser().getUsername())))
        .map(tr -> {
            exampleRepository.deleteById(example.getId());
            return example.getTranslation().getId();
        });
    }
}
