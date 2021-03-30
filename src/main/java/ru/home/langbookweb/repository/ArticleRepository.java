package ru.home.langbookweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.model.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> getAllByUser(User user, Pageable pageable);
    Article findArticleByUserAndId(User user, Long id);
}
