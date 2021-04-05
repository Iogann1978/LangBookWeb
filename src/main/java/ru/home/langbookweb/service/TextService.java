package ru.home.langbookweb.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.WordItem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextService {
    private Map<String, Integer> mapWords = new HashMap<>();
    @Autowired
    private WordService wordService;
    @Getter
    private Flux<WordItem> flux = Flux.empty();

    public Mono<Long> parse(String text) {
        mapWords.clear();
        Pattern p = Pattern.compile("\\d+");
        for (String s : text.split("\\W+")) {
            if (!s.isEmpty()) {
                String S = s.toLowerCase();
                Matcher m = p.matcher(S);
                if (!m.find()) {
                    mapWords.computeIfPresent(S, (k, v) -> ++v);
                    mapWords.computeIfAbsent(S, k -> 1);
                }
            }
        }

        flux = Flux.fromIterable(mapWords.entrySet())
                .flatMap(e -> wordService.isPresent(e.getKey())
                        .map(f -> f ? new WordItem(e.getKey(), 0) : new WordItem(e.getKey(), e.getValue())))
                .filter(wi -> wi.getCount() > 0).sort((wi1, wi2) -> wi2.getCount().equals(wi1.getCount()) ?
                        wi1.getWord().compareTo(wi2.getWord()) : wi2.getCount().compareTo(wi1.getCount()));
        return flux.count();
    }

    public Flux<WordItem> getPage(Pageable pageable) {
        return flux.collectList().flatMapMany(list -> Flux.fromIterable(
                list.stream().skip(pageable.getPageNumber() * pageable.getPageSize())
        .limit(pageable.getPageSize()).collect(Collectors.toList())));
    }
}
