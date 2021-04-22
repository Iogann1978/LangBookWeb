package ru.home.langbookweb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class LangbookwebApplicationTests {
    @Test
    public void testProcessWord() {
        log.info("word: {}", AbstractLoadService.processWord("a \"the word\" "));
    }
}
