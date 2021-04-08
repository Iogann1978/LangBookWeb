package ru.home.langbookweb.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String description;
    private String source;
    @OneToMany(mappedBy = "translation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Example> examples;
    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Word word;
}
