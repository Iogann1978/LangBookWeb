package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.model.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph("article")
    Page<Article> getAllByUser(User user, Pageable pageable);
    @EntityGraph("article")
    Article findArticleByUserAndId(User user, Long id);
}
