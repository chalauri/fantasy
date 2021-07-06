package ge.chalauri.fantasy.repositories;


import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {

    List<Team> findAll();

    Optional<Team> findByUserId(Integer userId);

    Optional<Team> findByUsername(String username);

    Optional<Team> findByName(String name);
}
