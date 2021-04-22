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
import ru.home.langbookweb.model.Verb;
import ru.home.langbookweb.model.xml.Card;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.VerbRepository;
import ru.home.langbookweb.repository.WordRepository;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class VerbsLoadService extends AbstractLoadService<Verb> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private VerbRepository verbRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    //@Test
    public void loadVerbs() throws JAXBException, IOException {
        User user = userRepository.findById("dicts").get();
        loadDicts(user, resourceLoader.getResource("classpath:tutor/verbs/"), false);
    }

    @Override
    @Transactional
    protected void saveEntry(User user, String source, String strWord, String tooltip, Card card) {
        String[] forms = strWord.split("/");
        Verb verb = Optional.of(wordRepository.findAllByUserAndWord(user, strWord, PageRequest.of(0, 10)))
                .filter(list -> !CollectionUtils.isEmpty(list)).map(list -> list.get(0))
                .flatMap(w -> verbRepository.getVerbById(w.getId()))
                .orElse(forms != null && forms.length >= 3 ?
                        Verb.builder().word(processWord(forms[0])).past(processWord(forms[1])).participle(processWord(forms[2])).tooltip(tooltip).user(user).build() :
                        Verb.builder().word(strWord).past("").participle("").tooltip(tooltip).user(user).build());
        fillEntry(verb, tooltip, source, card);
        verbRepository.saveAndFlush(verb);
    }
}
