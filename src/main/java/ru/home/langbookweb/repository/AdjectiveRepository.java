package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Adjective;

import java.util.Optional;

@Repository
public interface AdjectiveRepository extends JpaRepository<Adjective, Long> {
    @EntityGraph("word")
    Optional<Adjective> getAdjectiveById(Long id);
}
