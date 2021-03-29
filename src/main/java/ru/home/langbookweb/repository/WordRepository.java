package ru.home.langbookweb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    long countAllByUser(User user);
    List<Word> findAllByUserAndWord(User user, String word, Pageable pageable);
    List<Word> findAllByUser(User user, Pageable pageable);
    Word findWordByUserAndId(User user, Long id);
}
