package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.WordItem;

@Repository
public interface WordItemRepository extends JpaRepository<WordItem, Long> {
    @Transactional
    @Modifying
    void deleteAllByUser(User user);
    long countAllByUser(User user);
    @EntityGraph("wordItem")
    Page<WordItem> getAllByUser(User user, Pageable pageable);
    @EntityGraph("wordItem")
    WordItem findWordItemByUserAndId(User user, Long id);
}
