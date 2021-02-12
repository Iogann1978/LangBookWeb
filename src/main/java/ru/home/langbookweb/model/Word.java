package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
public class Word {
    protected String word;
    protected Set<Translation> translations;
    protected boolean roundRobin;
}
