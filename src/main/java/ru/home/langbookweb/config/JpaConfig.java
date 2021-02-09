package ru.home.langbookweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.home.langbookweb.repository"})
public class JpaConfig {
}
