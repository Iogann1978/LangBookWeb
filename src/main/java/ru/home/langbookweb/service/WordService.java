package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.repository.*;

@Service
@Slf4j
public class WordService {
    private WordRepository wordRepository;
    private NounRepository nounRepository;
    private VerbRepository verbRepository;
    private AdjectiveRepository adjectiveRepository;
    private AdverbRepository adverbRepository;
    private ParticipleRepository participleRepository;
    private PhraseRepository phraseRepository;
    private UserService userService;

    @Autowired
    public WordService(WordRepository wordRepository, NounRepository nounRepository,
                       VerbRepository verbRepository, AdjectiveRepository adjectiveRepository,
                       AdverbRepository adverbRepository, ParticipleRepository participleRepository,
                       PhraseRepository phraseRepository, UserService userService) {
        this.wordRepository = wordRepository;
        this.nounRepository = nounRepository;
        this.verbRepository = verbRepository;
        this.adjectiveRepository = adjectiveRepository;
        this.adverbRepository = adverbRepository;
        this.participleRepository = participleRepository;
        this.phraseRepository = phraseRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Flux<Word> getPage(String findWord, Pageable pageable) {
        Mono<User> user = userService.get();
        Flux<Word> words = user.flatMapIterable(u -> StringUtil.isEmpty(findWord) ? wordRepository.findAllByUser(u, pageable) :
            wordRepository.findWordFormByUserAndWord(u, findWord, pageable));
        return words;
    }

    @Transactional(readOnly = true)
    public Mono<Long> getCount() {
        Mono<User> user = userService.get();
        return user.map(u -> wordRepository.countAllByUser(u));
    }

    @Transactional(readOnly = true)
    public Mono<? extends Word> get(Long wordId) {
        Mono<User> user = userService.get();
        return user.map(u -> wordRepository.findWordByUserAndId(u, wordId))
            .map(word -> {
                if (word instanceof Noun)
                    return (Noun) word;
                else if (word instanceof Verb)
                    return (Verb) word;
                else if (word instanceof Adjective)
                    return (Adjective) word;
                else if (word instanceof Adverb)
                    return (Adverb) word;
                else if (word instanceof Participle)
                    return (Participle) word;
                else if (word instanceof Phrase)
                    return (Phrase) word;
                else
                    return word;
            });
    }

    @Transactional(readOnly = true)
    public Mono<Boolean> isPresent(String findWord) {
        Mono<User> user = userService.get();
        return user.map(u -> wordRepository.existsWordByUserAndWord(u, findWord));
    }

    @Transactional
    public <T extends Word> Mono<Long> save(T word) {
        Mono<User> user = userService.get();
        return user.map(u -> {
            if (word.getId() != null) {
                Word w = wordRepository.findWordByUserAndId(u, word.getId());
                word.setTranslations(w.getTranslations());
            }
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
    }

    @Transactional
    public Mono<Long> del(Word word) {
        Mono<User> user = userService.get();
        return user.map(u -> wordRepository.findWordByUserAndId(u, word.getId()))
                .map(w -> {
                    wordRepository.delete(w);
                    return w.getId();
                });
    }
}
