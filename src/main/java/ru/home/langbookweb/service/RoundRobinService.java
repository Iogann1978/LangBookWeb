package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.RoundRobin;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.repository.RoundRobinRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundRobinService {
    private RoundRobinRepository roundRobinRepository;
    private UserService userService;

    @Autowired
    public RoundRobinService(RoundRobinRepository roundRobinRepository, UserService userService) {
        this.roundRobinRepository = roundRobinRepository;
        this.userService = userService;
    }

    public Mono<RoundRobin> get() {
        Mono<User> user = userService.get();
        return user.map(u -> roundRobinRepository.findRoundRobinByUser(u));
    }

    public Mono<Long> addWord(Word word) {
        Mono<User> user = userService.get();
        return user.map(u -> roundRobinRepository.findRoundRobinByUser(u))
                .map(rr -> {
                    rr.getWords().add(word);
                    return roundRobinRepository.saveAndFlush(rr).getId();
                });
    }

    public Mono<Long> delWord(Word word) {
        Mono<User> user = userService.get();
        return user.map(u -> roundRobinRepository.findRoundRobinByUser(u))
                .map(rr -> {
                    List<Word> list = rr.getWords().stream().filter(w -> w.getId().equals(word.getId())).collect(Collectors.toList());
                    rr.getWords().removeAll(list);
                    return roundRobinRepository.saveAndFlush(rr).getId();
                });
    }
}
