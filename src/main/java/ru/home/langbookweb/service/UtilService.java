package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

@Slf4j
public class UtilService {
    public static Mono<String> getUser() {
        Mono<String> user = ReactiveSecurityContextHolder.getContext()
                .map(sc -> ((UserDetails) sc.getAuthentication().getPrincipal()).getUsername());
        return user;
    }
}
