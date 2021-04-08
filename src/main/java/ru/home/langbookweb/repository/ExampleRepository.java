package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Example;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
}
