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
import reactor.core.scheduler.Schedulers;
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
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<body>\n");
        sb.append(text);
        sb.append("</body>\n");
        sb.append("</html>");
        sb.replace(0, sb.length(), Pattern.compile("^(.+)$").matcher(sb).replaceAll("<p>$1</p>"));
        Flux.fromIterable(Arrays.asList(text.split("\n\r")))
                .flatMap(p -> Flux.fromIterable(Arrays.asList(p.split("\\s+")))
                    .flatMap(w -> wordService.isPresent(w.toLowerCase())
                            .flatMap(flag -> flag ? null : wordService.getPage(w, PageRequest.of(0, 1)).elementAt(0))
                            .filter(ww -> ww != null).map(ww -> Tuples.of(w, ww.getTooltip()))
                    ).doOnNext(tp -> {
                            Pattern pattern = Pattern.compile("(\\s+)"+tp.getT1()+"(\\s+)");
                        sb.replace(0, sb.length(), pattern.matcher(sb).replaceAll("$1<a href=\"#\" data-toggle=\"tooltip\" title=\""+tp.getT2()+"\">"+tp.getT1()+"</a>$2"));
                    })
                ).subscribeOn(Schedulers.immediate()).subscribe();
        return Mono.just(-1L);
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
