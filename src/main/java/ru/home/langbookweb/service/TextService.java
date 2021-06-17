package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import reactor.util.function.Tuples;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.WordItem;
import ru.home.langbookweb.repository.WordItemRepository;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TextService {
    private WordItemRepository wordItemRepository;
    private WordService wordService;
    private UserService userService;

    @Autowired
    public TextService(WordItemRepository wordItemRepository, WordService wordService, UserService userService) {
        this.wordItemRepository = wordItemRepository;
        this.wordService = wordService;
        this.userService = userService;
    }

    @Transactional
    public Mono<Long> parse(String text) {
        Mono<User> user = userService.get();
        return user.flatMap(u -> {
            wordItemRepository.deleteAllByUser(u);

            Map<String, Integer> mapWords = new HashMap<>();
            Pattern p = Pattern.compile("\\d+");
            for (String s : text.split("\\W+")) {
                if (!s.isEmpty()) {
                    String S = s.toLowerCase();
                    Matcher m = p.matcher(S);
                    if (!m.find()) {
                        mapWords.compute(S, (k, v) -> v == null ? 1 : ++v);
                    }
                }
            }

            return Flux.fromIterable(mapWords.entrySet())
                    .flatMap(e -> wordService.isPresent(e.getKey())
                            .map(f -> f ? WordItem.builder().word(e.getKey()).count(0).user(u).build() :
                                    WordItem.builder().word(e.getKey()).count(e.getValue()).user(u).build()))
                    .filter(wi -> wi.getCount() > 0)
                    .doOnNext(wi -> wordItemRepository.saveAndFlush(wi))
                    .count();
        });
    }

    @Transactional(readOnly = true)
    public Mono<Page<WordItem>> getPage(Pageable pageable) {
        Mono<User> user = userService.get();
        Mono<Page<WordItem>> page = user.map(u -> wordItemRepository.getAllByUser(u, pageable));
        return page;
    }

    public String textFromPdf(byte[] pdf) throws IOException {
        String result = "";
        try(RandomAccessRead rar = new RandomAccessBuffer(pdf)) {
            PDFParser parser = new PDFParser(rar);
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            try(PDDocument pdDoc = new PDDocument(cosDoc)) {
                result = pdfStripper.getText(pdDoc);
            }
        }
        return result;
    }

    public Mono<Long> translate(String text) {
        text = text.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;");
        Pattern p1 = Pattern.compile("^\t+(.+)$", Pattern.MULTILINE);
        Pattern p2 = Pattern.compile("[\\W\\s](\\w+)[\\W\\s]");

        Matcher m2 = p2.matcher(text);
        Set<String> words = new HashSet<>();
        while (m2.find()) {
            words.add(m2.group());
        }

        String ntext = text;
        return Flux.fromIterable(words)
                .filter(w -> !w.isBlank())
                .flatMap(w -> wordService.isPresent(w.trim().toLowerCase())
                        .flatMap(flag -> flag ? null : wordService.getPage(w, PageRequest.of(0, 1)).elementAt(0))
                        .filter(ww -> ww != null).map(ww -> Tuples.of(w, ww.getTooltip()))
                ).map(tp -> ntext.replaceAll(String.format("([\\W\\s])(%s)([\\W\\s])", tp.getT1()), String.format("$1<a href=\"#\" data-toggle=\"tooltip\" title=\"%s\">$2</a>$3", tp.getT2())))
                .doOnComplete(() -> {
                    String nntext = ntext.replaceAll("\t", " ");
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>\n");
                    sb.append("<body>\n");
                    Matcher m1 = p1.matcher(nntext);
                    sb.append(m1.replaceAll("<p>$1</p>"));
                    sb.append("</body>\n");
                    sb.append("</html>\n");
                }).count();
    }

    @Transactional
    public Mono<String> del(WordItem wordItem) {
        Mono<User> user = userService.get();
        return user.map(u -> wordItemRepository.findWordItemByUserAndId(u, wordItem.getId()))
                .map(wi -> {
                    wordItemRepository.delete(wi);
                    return wi.getWord();
                });
    }

    @Transactional(readOnly = true)
    public Mono<Long> getCount() {
        Mono<User> user = userService.get();
        return user.map(u -> wordItemRepository.countAllByUser(u));
    }
}
