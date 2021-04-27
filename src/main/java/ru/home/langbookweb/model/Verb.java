package ru.home.langbookweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class Verb extends Word {
    @NotNull
    @Column(nullable = false)
    private String past;
    @NotNull
    @Column(nullable = false)
    private String participle;

    public static String getEndingS(String verb) {
        if (verb == null)
            return null;
        if ("have".equals(verb))
            return "has";
        if ("am".equals(verb))
            return "is";
        if (verb.endsWith("o") || verb.endsWith("x") || verb.endsWith("sh") || verb.endsWith("ch") || verb.endsWith("tch")
                || verb.endsWith("zz") || verb.endsWith("s"))
            return verb + "es";
        if (verb.endsWith("y"))
            return verb.substring(0, verb.length() - 1) + "ies";
        return verb + "s";
    }
}
