package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Noun extends Word {
    private String plural;
}
