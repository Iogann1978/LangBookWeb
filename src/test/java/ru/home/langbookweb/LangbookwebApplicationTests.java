package ru.home.langbookweb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Verb;
import ru.home.langbookweb.model.WordItem;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.WordItemRepository;
import ru.home.langbookweb.repository.WordRepository;

import java.util.Iterator;

@SpringBootTest
@Slf4j
class LangbookwebApplicationTests {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordItemRepository wordItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testProcessWord() {
        log.info("word: {}", AbstractLoadService.processWord("a \"the word\" "));
    }

    @Test
    public void recalcText() {
        User user = userRepository.findById("user").get();
        long count = wordItemRepository.countAllByUser(user);
        Page<WordItem> page = wordItemRepository.getAllByUser(user, PageRequest.of(0, (int)count));
        Iterator<WordItem> iter = page.iterator();
        while (iter.hasNext()) {
            WordItem wi = iter.next();
            if (wordRepository.existsWordByUserAndWord(user, wi.getWord())) {
                log.info("del wordItem: {}", wi.getWord());
                wordItemRepository.delete(wi);
            }
        }
    }

    //@Test
    public void testVerbEndingS() {
        Verb v1 = Verb.builder().word("dry").build();
        log.info("verb: {} {}", v1.getWord(), Verb.getEndingS(v1.getWord()));
        Verb v2 = Verb.builder().word("do").build();
        log.info("verb: {} {}", v2.getWord(), Verb.getEndingS(v2.getWord()));
        Verb v3 = Verb.builder().word("kiss").build();
        log.info("verb: {} {}", v3.getWord(), Verb.getEndingS(v3.getWord()));
    }
}
