package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Phrase;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {
}
