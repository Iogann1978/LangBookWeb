package ru.home.langbookweb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Word;

import java.util.List;

@Repository
public interface DictionaryRepository extends PagingAndSortingRepository<Word, Long> {
    List<Word> findAllByWord(String word, Pageable pageable);
}
