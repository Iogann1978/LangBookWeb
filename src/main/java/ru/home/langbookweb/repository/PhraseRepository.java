package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.model.User;

import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    @EntityGraph("word")
    Page<Phrase> getAllByUser(User user, Pageable pageable);
    @EntityGraph("word")
    Optional<Phrase> getPhraseById(Long id);
}
