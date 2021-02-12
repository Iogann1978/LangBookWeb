package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Translation {
    private String description;
    private String example;
}
