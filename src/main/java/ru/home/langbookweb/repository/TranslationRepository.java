package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Translation;
import ru.home.langbookweb.model.User;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    @EntityGraph("translation.examples")
    @Query("select t from Translation t join Word w on t.word = w join User u on w.user = u where u = ?1 and t.id = ?2")
    Translation getTranslationByUserAndId(User user, Long id);
}
