package ru.home.langbookweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
    public Mono<Page<Article>> getArticles(Pageable pageable) {
        Mono<User> user = userService.getUser();
        Mono<Page<Article>> page = user.map(u -> articleRepository.getAllByUser(u, pageable));
        return page;
    }

    @Transactional
    public Mono<Long> save(Article article) {
        Mono<User> user = userService.getUser();
        return user.map(u -> {
            article.setUser(u);
            return articleRepository.saveAndFlush(article);
        }).map(Article::getId);
    }

    @Transactional
    public void del(Article article) {
        Mono<User> user = userService.getUser();
        user.map(u -> articleRepository.findArticleByUserAndId(u, article.getId()))
                .doOnNext(a -> articleRepository.delete(a))
                .subscribeOn(Schedulers.immediate()).subscribe();;
    }
}
