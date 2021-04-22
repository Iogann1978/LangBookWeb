package ru.home.langbookweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.model.xml.Card;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.WordRepository;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@SpringBootTest
public class WordsLoadService extends AbstractLoadService<Word> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    //@Test
    public void loadWords() throws JAXBException, IOException {
        User user = userRepository.findById("dicts").get();
        loadDicts(user, resourceLoader.getResource("classpath:tutor/"), false);
    }

    @Override
    @Transactional
    protected void saveEntry(User user, String source, String strWord, String tooltip, Card card) {
        Word word = wordRepository.existsWordByUserAndWord(user, strWord) ?
                wordRepository.findAllByUserAndWord(user, strWord, PageRequest.of(0, 10)).get(0) :
                Word.builder().word(strWord).tooltip(tooltip).user(user).build();
        fillEntry(word, tooltip, strWord, card);
        wordRepository.saveAndFlush(word);
    }
}
