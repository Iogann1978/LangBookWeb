package ru.home.langbookweb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;
import ru.home.langbookweb.repository.UserRepository;
import ru.home.langbookweb.repository.WordRepository;
import ru.home.langbookweb.service.TextService;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
public class ProcessPdfTest {
    @Autowired
    private TextService textService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private UserRepository userRepository;

    //@Test
    public void pdfToHtmlTest() throws IOException {
        User user = userRepository.findById("user").get();
        byte[] bytes = resourceLoader.getResource("classpath:Harry-potter-sorcerers-stone.pdf").getInputStream().readAllBytes();
        String text = textService.textFromPdf(bytes);
        text = text.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;");

        Pattern p1 = Pattern.compile("^\t+(.+)$", Pattern.MULTILINE);
        Pattern p2 = Pattern.compile("[\\W\\s](\\w+)[\\W\\s]");

        Matcher m2 = p2.matcher(text);
        Map<String, String> words = new HashMap<>();
        while (m2.find()) {
            String key = m2.group();
            String ww = key.trim().toLowerCase();
            if (!words.containsKey(key) && wordRepository.existsWordByUserAndWord(user, ww)) {
                List<Word> list = wordRepository.findWordFormByUserAndWord(user, ww, PageRequest.of(0, 10));
                if (!CollectionUtils.isEmpty(list)) {
                    Word word = list.get(0);
                    words.put(key, word.getTooltip());
                }
            }
        }

        for (Map.Entry<String, String> e : words.entrySet()) {
            text = text.replaceAll(String.format("([\\W\\s])(%s)([\\W\\s])", e.getKey()), String.format("$1<a href=\"#\" data-toggle=\"tooltip\" title=\"%s\">$2</a>$3", e.getValue()));
        }

        Matcher m1 = p1.matcher(text);
        text = m1.replaceAll("<p>$1</p>");
        text = text.replaceAll("\t", " ");
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("<body>\n");
        sb.append(text);
        sb.append("</body>\n");
        sb.append("</html>\n");
        /*
        while (m1.find()) {
            sb.append("<p>\n");
            String line = m1.group();
            Matcher m2 = p2.matcher(line);
           while (m2.find()) {
               String s1 = m2.group(1);
               String w = m2.group(2);
               String s2 = m2.group(3);
               sb.append(s1);
               String ww = w.trim().toLowerCase();
                if (!w.isBlank() && wordRepository.existsWordByUserAndWord(user, ww)) {
                    List<Word> words = wordRepository.findWordFormByUserAndWord(user, ww, PageRequest.of(0, 10));
                    if (!CollectionUtils.isEmpty(words)) {
                        Word word = words.get(0);
                        sb.append(String.format("<a href=\"#\" data-toggle=\"tooltip\" title=\"%s\">%s</a>", w, word.getTooltip()));
                    }
                } else {
                    sb.append(w);
                }
               sb.append(s2);
            }
            sb.append("</p>\n");
        }
         */
        log.info(sb.toString());
        //textService.translate(text).subscribeOn(Schedulers.immediate()).subscribe();
    }
}
