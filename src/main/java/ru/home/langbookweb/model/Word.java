package ru.home.langbookweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @NotNull
    @Column(nullable = false)
    protected String word;
    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = false)
    protected Set<Translation> translations;
    @NotNull
    @Column(nullable = false)
    protected String tooltip;
    @NotNull
    @ManyToOne(optional = false)
    protected User user;
}
