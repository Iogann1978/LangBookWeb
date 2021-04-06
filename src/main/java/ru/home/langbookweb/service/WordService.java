package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.repository.*;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class WordService {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private NounRepository nounRepository;
    @Autowired
    private VerbRepository verbRepository;
    @Autowired
    private AdjectiveRepository adjectiveRepository;
    @Autowired
    private AdverbRepository adverbRepository;
    @Autowired
    private ParticipleRepository participleRepository;
    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public Flux<? super Word> getWords(String findWord, Pageable pageable) {
        Mono<User> user = userService.getUser();
        Flux<Word> words = user.flatMapIterable(u -> Strings.isEmpty(findWord) ? wordRepository.findAllByUser(u, pageable) :
                            wordRepository.findAllByUserAndWord(u, findWord, pageable)
                ).map(word -> {
                    Optional<Noun> noun = nounRepository.findById(word.getId());
                    if (noun.isPresent()) {
                        return noun.get();
                    }
                    Optional<Verb> verb = verbRepository.findById(word.getId());
                    if (verb.isPresent()) {
                        return verb.get();
                    }
                    Optional<Adjective> adjective = adjectiveRepository.findById(word.getId());
                    if (adjective.isPresent()) {
                        return adjective.get();
                    }
                    Optional<Adverb> adverb = adverbRepository.findById(word.getId());
                    if (adverb.isPresent()) {
                        return adverb.get();
                    }
                    Optional<Participle> participle = participleRepository.findById(word.getId());
                    if (participle.isPresent()) {
                        return participle.get();
                    }
                    Optional<Phrase> phrase = phraseRepository.findById(word.getId());
                    if (phrase.isPresent()) {
                        return phrase.get();
                    }
                    return word;
                });
        return words;
    }

    @Transactional(readOnly = true)
    public Mono<Long> getCount() {
        Mono<User> user = userService.getUser();
        return user.map(u -> wordRepository.countAllByUser(u));
    }

    @Transactional(readOnly = true)
    public Mono<? super Word> get(Long wordId) {
        Mono<User> user = userService.getUser();
        return user.map(u -> wordRepository.findWordByUserAndId(u, wordId))
            .map(word -> {
                Optional<Noun> noun = nounRepository.findById(word.getId());
                if (noun.isPresent()) {
                    return noun.get();
                }
                Optional<Verb> verb = verbRepository.findById(word.getId());
                if (verb.isPresent()) {
                    return verb.get();
                }
                Optional<Adjective> adjective = adjectiveRepository.findById(word.getId());
                if (adjective.isPresent()) {
                    return adjective.get();
                }
                Optional<Adverb> adverb = adverbRepository.findById(word.getId());
                if (adverb.isPresent()) {
                    return adverb.get();
                }
                Optional<Participle> participle = participleRepository.findById(word.getId());
                if (participle.isPresent()) {
                    return participle.get();
                }
                Optional<Phrase> phrase = phraseRepository.findById(word.getId());
                if (phrase.isPresent()) {
                    return phrase.get();
                }
                return word;
            });
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> isPresent(String findWord) {
        Mono<User> user = userService.getUser();
        return user.map(u -> wordRepository.existsWordByUserAndWord(u, findWord));
    }

    @Transactional
    public <T extends Word> Mono<Long> save(T word) {
        Mono<User> user = userService.getUser();
        Mono<Long> wid = user.map(u -> {
                Long id = null;
                word.setUser(u);
                if (word instanceof Noun) {
                    Noun nword = nounRepository.saveAndFlush((Noun) word);
                    id = nword.getId();
                } else if (word instanceof Verb) {
                    Verb nword = verbRepository.saveAndFlush((Verb) word);
                    id = nword.getId();
                } else if (word instanceof Adjective) {
                    Adjective nword = adjectiveRepository.saveAndFlush((Adjective) word);
                    id = nword.getId();
                } else if (word instanceof Adverb) {
                    Adverb nword = adverbRepository.saveAndFlush((Adverb) word);
                    id = nword.getId();
                } else if (word instanceof Participle) {
                    Participle nword = participleRepository.saveAndFlush((Participle) word);
                    id = nword.getId();
                } else if (word instanceof Phrase) {
                    Phrase nword = phraseRepository.saveAndFlush((Phrase) word);
                    id = nword.getId();
                } else {
                    Word nword = wordRepository.saveAndFlush((Word) word);
                    id = nword.getId();
                }
                return id;
            });
        return wid;
    }

    @Transactional
    public void del(Word word) {
        Mono<User> user = userService.getUser();
        user.map(u -> wordRepository.findWordByUserAndId(u, word.getId()))
                .doOnNext(w -> wordRepository.delete(w))
                .subscribeOn(Schedulers.immediate()).subscribe();;
    }
}
