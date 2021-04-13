package ru.home.langbookweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "article", attributeNodes = @NamedAttributeNode("user"))
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String filename;
    @Lob
    @Column(length = 100000)
    private byte[] text;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    protected User user;
}
