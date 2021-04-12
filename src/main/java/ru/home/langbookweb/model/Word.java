package ru.home.langbookweb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedEntityGraph(name = "word",
        attributeNodes = {
            @NamedAttributeNode(value = "translations", subgraph = "translations.examples"),
            @NamedAttributeNode("user")
        },
        subgraphs = @NamedSubgraph(name = "translations.examples", attributeNodes = @NamedAttributeNode("examples"))
)
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @NotNull
    @Column(nullable = false)
    protected String word;

    @NotNull
    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected Set<Translation> translations;
    @NotNull
    @Column(nullable = false)
    protected String tooltip;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    protected User user;

    public void setWord(String word) {
        this.word = word.toLowerCase();
    }
}
