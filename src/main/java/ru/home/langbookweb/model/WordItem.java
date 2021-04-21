package ru.home.langbookweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "wordItem", attributeNodes = @NamedAttributeNode("user"))
public class WordItem {
    @Id
    private String word;
    private Integer count;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    protected User user;
}
