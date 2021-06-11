package ru.home.langbookweb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.home.langbookweb.model.*;
import ru.home.langbookweb.repository.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SpringBootTest
@Slf4j
class LangbookwebApplicationTests {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private NounRepository nounRepository;
    @Autowired
    private VerbRepository verbRepository;
    @Autowired
    private AdjectiveRepository adjectiveRepository;
    @Autowired
    private AdverbRepository adverbRepository;
    @Autowired
    private ParticipleRepository participleRepository;
    @Autowired
    private PhraseRepository phraseRepository;
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

    @Test
    public void firstDictionary() {
        User user = userRepository.findById("user").get();
        long count = wordRepository.countAllByUser(user);
        List<Word> words = wordRepository.findAllByUser(user, PageRequest.of(0, (int)count, Sort.by("word").and(Sort.by("id"))));
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("first_dict.html"))) {
            bw.write("<html>");
            bw.newLine();
            bw.write("<body>");
            bw.newLine();
            bw.write("<table border=\"1\">");
            bw.newLine();
            bw.write("<tr><th>Слово</th><th>Перевод</th></tr>");
            bw.newLine();
            for (Word word : words) {
                StringBuilder sb = new StringBuilder();
                for (Translation translation : word.getTranslations()) {
                    if ("первый".equals(translation.getSource())) {
                        if (sb.length() > 0)
                            sb.append("<br>");
                        sb.append(translation.getDescription());
                    }
                }
                if (sb.length() > 0) {
                    bw.write("<tr>");
                    bw.newLine();
                    bw.write("<td>");
                    bw.write(word.getWord());
                    nounRepository.getNounById(word.getId()).ifPresent(noun -> {
                        try {
                            bw.write(String.format("<i>(noun)</i> (%s)", noun.getPlural()));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
                    verbRepository.getVerbById(word.getId()).ifPresent(verb -> {
                        try {
                            bw.write(String.format("<i>(verb)</i> (%s, %s)", verb.getPast(), verb.getParticiple()));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
                    adjectiveRepository.getAdjectiveById(word.getId()).ifPresent(adjective -> {
                        try {
                            bw.write(String.format("<i>(adjective)</i> (%s, %s)", adjective.getComparative(), adjective.getSuperative()));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
                    bw.write("</td>");
                    bw.newLine();
                    bw.write("<td>");
                    bw.newLine();
                    bw.write(sb.toString());
                    bw.newLine();
                    bw.write("</td>");
                    bw.write("</tr>");
                }
            }
            bw.write("</table>");
            bw.newLine();
            bw.write("</body>");
            bw.newLine();
            bw.write("</html>");
            bw.newLine();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testDuplicates() {
        User user = userRepository.findById("user").get();
        long count = wordRepository.countAllByUser(user);
        List<Word> words = wordRepository.findAllByUser(user, PageRequest.of(0, (int)count, Sort.by("word").and(Sort.by("id"))));
        Map<String, Set<String>> map = new HashMap<>();
        map.put("word", new HashSet<>());
        map.put("noun", new HashSet<>());
        map.put("verb", new HashSet<>());
        map.put("adjective", new HashSet<>());
        map.put("adverb", new HashSet<>());
        map.put("participle", new HashSet<>());
        map.put("phrase", new HashSet<>());
        for (Word word : words) {
            String type = "word";
            if (nounRepository.existsById(word.getId())) {
                type = "noun";
            } else if (verbRepository.existsById(word.getId())) {
                type = "verb";
            } else if (adjectiveRepository.existsById(word.getId())) {
                type = "adjective";
            } else if (adverbRepository.existsById(word.getId())) {
                type = "adverb";
            } else if (participleRepository.existsById(word.getId())) {
                type = "participle";
            } else if (phraseRepository.existsById(word.getId())) {
                type = "phrase";
            }
            Set set = map.get(type);
            if (set.contains(word.getWord())) {
                log.info("duplicate for word {} type {}", word.getWord(), type);
            } else {
                set.add(word.getWord());
            }
        }
    }
}
