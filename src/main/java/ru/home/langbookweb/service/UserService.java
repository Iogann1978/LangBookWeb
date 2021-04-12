package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Mono<User> get() {
        return ReactiveSecurityContextHolder.getContext()
                .map(sc -> ((UserDetails) sc.getAuthentication().getPrincipal()).getUsername())
                .map(username -> userRepository.findById(username))
                .filter(u -> u.isPresent()).map(u -> u.get());
    }

    @Transactional(readOnly = true)
    public List<UserDetails> getAll() {
        return userRepository.findUsersByEnabled(true).stream().map(u -> (UserDetails) u).collect(Collectors.toList());
    }
}
