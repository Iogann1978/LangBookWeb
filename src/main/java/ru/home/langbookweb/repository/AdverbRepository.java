package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Adverb;

@Repository
public interface AdverbRepository extends JpaRepository<Adverb, Long> {
}
