package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
}
