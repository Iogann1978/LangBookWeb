package ru.home.langbookweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    private String login;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Word> dictionary;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Article> articles;
    @NotNull
    @OneToOne(mappedBy = "user", optional = false, fetch = FetchType.LAZY)
    private RoundRobin roundRobin;
}
