package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Adverb;

import java.util.Optional;

@Repository
public interface AdverbRepository extends JpaRepository<Adverb, Long> {
    @EntityGraph("word")
    Optional<Adverb> getAdverbById(Long id);
}
