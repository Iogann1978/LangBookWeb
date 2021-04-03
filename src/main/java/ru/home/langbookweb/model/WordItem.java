package ru.home.langbookweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordItem {
    private String word;
    private Integer count;
}
