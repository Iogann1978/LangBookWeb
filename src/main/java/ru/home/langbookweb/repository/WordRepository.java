package ru.home.langbookweb.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.model.Word;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    long countAllByUser(User user);
    @EntityGraph("word")
    List<Word> findAllByUserAndWord(User user, String word, Pageable pageable);
    @EntityGraph("word")
    List<Word> findAllByUser(User user, Pageable pageable);
    @EntityGraph("word")
    Word findWordByUserAndId(User user, Long id);
    @Query("select case when count(w) > 0 then true else false end from Word w " +
            "left join Noun n on w.id = n.id and w.user = n.user " +
            "left join Verb v on w.id = v.id and w.user = v.user " +
            "left join Adjective a on w.id = a.id and w.user = a.user " +
            "where w.user = ?1 and ?2 in (w.word, n.plural, v.participle, v.past, getEndingS(v.word), a.comparative, a.superative)")
    boolean existsWordByUserAndWord(User user, String word);
}
