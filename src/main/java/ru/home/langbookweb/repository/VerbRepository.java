package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Verb;

@Repository
public interface VerbRepository extends JpaRepository<Verb, Long> {
}
