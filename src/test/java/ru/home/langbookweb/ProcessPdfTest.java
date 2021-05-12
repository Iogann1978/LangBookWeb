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
import java.util.List;
import java.util.Optional;
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

    @Test
    public void pdfToHtmlTest() throws IOException {
        User user = userRepository.findById("user").get();
        byte[] bytes = resourceLoader.getResource("classpath:Harry-potter-sorcerers-stone.pdf").getInputStream().readAllBytes();
        String text = textService.textFromPdf(bytes);
        Pattern p1 = Pattern.compile("^.+$", Pattern.MULTILINE);
        Pattern p2 = Pattern.compile("([\\W\\s])(\\w+)([\\W\\s])", Pattern.MULTILINE);
        Matcher m1 = p1.matcher(text);
        StringBuilder sb = new StringBuilder();
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
                    List<Word> words = wordRepository.findWordFormByUserAndWord(user, ww);
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
        log.info(sb.toString());
        //textService.translate(text).subscribeOn(Schedulers.immediate()).subscribe();
    }
}
