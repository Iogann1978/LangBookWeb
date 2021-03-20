package ru.home.langbookweb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Word;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findAllByUserAndWord(String login, String word, Pageable pageable);
    List<Word> findAllByUser(String login, Pageable pageable);
    Word findWordByUserAndId(String login, Long id);
}
