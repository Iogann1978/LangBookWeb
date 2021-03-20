package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Participle;

@Repository
public interface ParticipleRepository extends JpaRepository<Participle, Long> {
}
