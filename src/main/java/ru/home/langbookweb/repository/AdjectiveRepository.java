package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Adjective;

@Repository
public interface AdjectiveRepository extends JpaRepository<Adjective, Long> {
}
