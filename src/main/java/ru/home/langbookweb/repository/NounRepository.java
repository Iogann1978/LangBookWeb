package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Noun;

import java.util.Optional;

@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {
    @EntityGraph("word")
    Optional<Noun> getNounById(Long id);
}
