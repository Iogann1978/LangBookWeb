package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Phrase;
import ru.home.langbookweb.model.User;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    Page<Phrase> getAllByUser(User user, Pageable pageable);
}
