package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.WordItem;

@Repository
public interface WordItemRepository extends JpaRepository<WordItem, String> {
    void deleteAllByUser(User user);
    long countAllByUser(User user);
    @EntityGraph("wordItem")
    Page<WordItem> getAllByUser(User user, Pageable pageable);
    @EntityGraph("wordItem")
    WordItem findWordItemByUserAndWord(User user, String word);
}
