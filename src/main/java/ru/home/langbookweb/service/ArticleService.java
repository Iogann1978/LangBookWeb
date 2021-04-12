package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.home.langbookweb.model.Article;
import ru.home.langbookweb.model.User;
import ru.home.langbookweb.repository.ArticleRepository;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public Mono<Page<Article>> getPage(Pageable pageable) {
        Mono<User> user = userService.get();
        Mono<Page<Article>> page = user.map(u -> articleRepository.getAllByUser(u, pageable));
        return page;
    }

    @Transactional(readOnly = true)
    public Mono<Article> get(Long id) {
        Mono<User> user = userService.get();
        return user.map(u -> articleRepository.findArticleByUserAndId(u, id));
    }

    @Transactional
    public Mono<Long> save(Article article) {
        Mono<User> user = userService.get();
        return user.map(u -> {
            article.setUser(u);
            return articleRepository.saveAndFlush(article);
        }).map(Article::getId);
    }

    @Transactional
    public Mono<Long> del(Article article) {
        Mono<User> user = userService.get();
        return user.map(u -> articleRepository.findArticleByUserAndId(u, article.getId()))
                .map(a -> {
                    articleRepository.delete(a);
                    return a.getId();
                });
    }
}
