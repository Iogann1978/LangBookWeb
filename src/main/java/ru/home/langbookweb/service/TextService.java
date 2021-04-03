package ru.home.langbookweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;
import ru.home.langbookweb.model.WordItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextService {
    private Map<String, Integer> mapWords = new HashMap<>();
    private List<WordItem> words = new ArrayList<>();
    @Autowired
    private WordService wordService;

    public void parse(String text) {
        mapWords.clear();
        Pattern p = Pattern.compile("\\d+");
        for (String s : text.split("\\W+")) {
            if (!s.isEmpty()) {
                String S = s.toLowerCase();
                Matcher m = p.matcher(S);
                if (!m.find() && !wordService.isPresent(S)) {
                    mapWords.computeIfPresent(S, (k, v) -> ++v);
                    mapWords.computeIfAbsent(S, k -> 1);
                }
            }
        }

        words.clear();
        words.addAll(mapWords.entrySet().stream().map(e -> new WordItem(e.getKey(), e.getValue()))
                .sorted((w1, w2) -> {
                    if (w2.getCount().equals(w1.getCount()))
                        return w1.getWord().compareTo(w2.getWord());
                    else
                        return w2.getCount().compareTo(w1.getCount());
                }).collect(Collectors.toList()));
    }

    public PagedListHolder<WordItem> getPage(Pageable pageable) {
        PagedListHolder<WordItem> page = new PagedListHolder<>(words);
        page.setPageSize(pageable.getPageSize());
        page.setPage(pageable.getPageNumber());
        return page;
    }
}
