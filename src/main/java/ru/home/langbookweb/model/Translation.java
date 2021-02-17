package ru.home.langbookweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    private String description;
    @OneToMany
    private Set<Example> examples;
}
