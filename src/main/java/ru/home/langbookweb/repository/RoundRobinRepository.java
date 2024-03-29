package ru.home.langbookweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.RoundRobin;
import ru.home.langbookweb.model.User;

@Repository
public interface RoundRobinRepository extends JpaRepository<RoundRobin, Long> {
    @EntityGraph("roundrobin")
    RoundRobin findRoundRobinByUser(User user);
}
