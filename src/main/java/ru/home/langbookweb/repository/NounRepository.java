package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Noun;

@Repository
public interface NounRepository extends JpaRepository<Noun, Long> {
}
