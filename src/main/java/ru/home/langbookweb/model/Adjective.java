package ru.home.langbookweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Adjective extends Word {
    @NotNull
    @Column(nullable = false)
    private String comparative;
    @NotNull
    @Column(nullable = false)
    private String superative;
}
