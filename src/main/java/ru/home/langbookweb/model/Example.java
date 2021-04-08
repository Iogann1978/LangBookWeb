package ru.home.langbookweb.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text1;
    private String text2;
    @ManyToOne
    @JoinColumn(name = "translation_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Translation translation;
}
