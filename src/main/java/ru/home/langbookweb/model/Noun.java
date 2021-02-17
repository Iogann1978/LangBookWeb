package ru.home.langbookweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Noun extends Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    private String plural;
}
