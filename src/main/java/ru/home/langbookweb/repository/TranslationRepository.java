package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
}
