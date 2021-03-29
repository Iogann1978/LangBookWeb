package ru.home.langbookweb.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.UserRepository;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Getter
    private Mono<User> user;

    @PostConstruct
    public void init() {
        user = ReactiveSecurityContextHolder.getContext()
                .map(sc -> ((UserDetails) sc.getAuthentication().getPrincipal()).getUsername())
                .map(username -> userRepository.findById(username))
                .filter(u -> u.isPresent()).map(u -> u.get());
    }
}
