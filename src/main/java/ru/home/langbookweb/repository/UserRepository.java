package ru.home.langbookweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findUsersByEnabled(boolean enabled);
}
