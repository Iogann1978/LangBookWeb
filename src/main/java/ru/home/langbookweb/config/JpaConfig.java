package ru.home.langbookweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.home.langbookweb.repository"})
@EnableTransactionManagement
public class JpaConfig {
}
