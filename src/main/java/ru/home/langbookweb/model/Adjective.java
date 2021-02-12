package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Adjective extends Word {
    private String comparative;
    private String superative;
}
