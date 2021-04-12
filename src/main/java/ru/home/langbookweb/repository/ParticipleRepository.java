package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Participle;

import java.util.Optional;

@Repository
public interface ParticipleRepository extends JpaRepository<Participle, Long> {
    @EntityGraph("word")
    Optional<Participle> getParticipleById(Long id);
}
