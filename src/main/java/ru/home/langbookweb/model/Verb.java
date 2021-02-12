package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Verb extends Word {
    private String past;
    private String participle;
}
