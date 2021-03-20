package ru.home.langbookweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.langbookweb.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
