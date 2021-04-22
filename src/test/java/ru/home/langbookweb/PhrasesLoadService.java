package ru.home.langbookweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.xml.Card;
import ru.home.langbookweb.repository.PhraseRepository;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.WordRepository;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class PhrasesLoadService extends AbstractLoadService<Phrase> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    //@Test
    public void loadPhrases() throws JAXBException, IOException {
        User user = userRepository.findById("dicts").get();
        loadDicts(user, resourceLoader.getResource("classpath:tutor/"), true);
    }

    @Override
    @Transactional
    protected void saveEntry(User user, String source, String strWord, String tooltip, Card card) {
        Phrase phrase = Optional.of(wordRepository.findAllByUserAndWord(user, strWord, PageRequest.of(0, 10)))
                .filter(list -> !CollectionUtils.isEmpty(list)).map(list -> list.get(0))
                .flatMap(w -> phraseRepository.getPhraseById(w.getId()))
                .orElse(Phrase.builder().word(strWord).tooltip(tooltip).user(user).build());
        fillEntry(phrase, tooltip, source, card);
        phraseRepository.saveAndFlush(phrase);
    }
}
