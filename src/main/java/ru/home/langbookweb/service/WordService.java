package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.repository.*;

import java.util.Optional;

@Service
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
    private UserRepository userRepository;

    public Flux<? super Word> getWords(Mono<String> user, String findWord, Pageable pageable) {
        Flux<Word> words = user.map(username -> userRepository.findById(username))
                .filter(u -> u.isPresent()).map(u -> u.get())
                .flatMapIterable(u -> findWord.isEmpty() ? wordRepository.findAllByUser(u.getLogin(), pageable) :
                        wordRepository.findAllByUserAndWord(u.getLogin(), findWord, pageable))
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
        return words;
    }

    public Mono<Word> getWord(Mono<String> user, Long wordId) {
        return user.map(username -> userRepository.findById(username))
                .filter(u -> u.isPresent()).map(u -> u.get())
                .map(u -> wordRepository.findWordByUserAndId(u.getLogin(), wordId));
    }

    public <T extends Word> Mono<Long> saveWord(Mono<String> user, T word) {
        Mono<Long> wid = user.map(username -> userRepository.findById(username))
            .filter(u -> u.isPresent()).map(u -> u.get()).map(u -> {
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

    public void delWord(Mono<String> user, Word word) {
        user.map(username -> userRepository.findById(username))
                .filter(u -> u.isPresent()).map(u -> u.get())
                .map(u -> wordRepository.findWordByUserAndId(u.getLogin(), word.getId()))
                .doOnNext(w -> wordRepository.delete(w))
                .subscribeOn(Schedulers.immediate()).subscribe();;
    }
}
