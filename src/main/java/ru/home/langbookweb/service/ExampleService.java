package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Example;
import ru.home.langbookweb.repository.ExampleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;
    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public Flux<Example> getExamples(Long translationId) {
        return translationService.get(translationId).flatMapIterable(trs -> trs.getExamples());
    }

    @Transactional
    public Mono<Long> save(Example example) {
        Example e = example.getId() == null ?
                example : exampleRepository.getOne(example.getId());
        if (example.getTranslation() == null) example.setTranslation(e.getTranslation());
        return translationService.get(example.getTranslation().getId())
                .map(t -> exampleRepository.saveAndFlush(example))
                .map(exmp -> exmp.getTranslation().getId());
    }

    @Transactional
    public Mono<Long> del(Long translationId, Long exampleId) {
        return translationService.get(translationId).flatMap(t -> {
            List<Example> examples = t.getExamples().stream().filter(e -> e.getId().equals(exampleId)).collect(Collectors.toList());
            t.getExamples().removeAll(examples);
            return translationService.save(t);
        });
    }
}
