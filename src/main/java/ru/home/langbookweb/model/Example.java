package ru.home.langbookweb.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Example {
    private String text1;
    private String text2;
}
