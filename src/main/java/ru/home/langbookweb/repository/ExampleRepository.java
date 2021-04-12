package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Example;
import ru.home.langbookweb.model.User;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    @EntityGraph("example")
    @Query("select e from Example e join Translation t on e.translation = t join Word w on t.word = w join User u on w.user = u where u = ?1 and t.id = ?2")
    Example getExampleByUserAndId(User user, Long ig);
}
