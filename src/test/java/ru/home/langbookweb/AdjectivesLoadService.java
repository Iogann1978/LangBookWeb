package ru.home.langbookweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.home.langbookweb.model.Adjective;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.xml.Card;
import ru.home.langbookweb.repository.AdjectiveRepository;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.WordRepository;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class AdjectivesLoadService extends AbstractLoadService<Adjective>{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private AdjectiveRepository adjectiveRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    //@Test
    public void loadAdjectives() throws JAXBException, IOException {
        User user = userRepository.findById("dicts").get();
        loadDicts(user, resourceLoader.getResource("classpath:tutor/adjectives/"), false);
    }

    @Override
    @Transactional
    protected void saveEntry(User user, String source, String strWord, String tooltip, Card card) {
        Adjective adjective = Optional.of(wordRepository.findAllByUserAndWord(user, strWord, PageRequest.of(0, 10)))
                .filter(list -> !CollectionUtils.isEmpty(list)).map(list -> list.get(0))
                .flatMap(w -> adjectiveRepository.getAdjectiveById(w.getId()))
                .orElse(Adjective.builder().word(strWord).tooltip(tooltip).comparative("more " + strWord).superative("most " + strWord).user(user).build());
        fillEntry(adjective, tooltip, source, card);
        adjectiveRepository.saveAndFlush(adjective);
    }
}
