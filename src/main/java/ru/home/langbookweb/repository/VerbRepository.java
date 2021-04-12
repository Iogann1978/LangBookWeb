package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Verb;

import java.util.Optional;

@Repository
public interface VerbRepository extends JpaRepository<Verb, Long> {
    @EntityGraph("word")
    Optional<Verb> getVerbById(Long id);
}
