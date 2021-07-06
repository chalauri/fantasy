package ge.chalauri.fantasy.repositories;


import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
