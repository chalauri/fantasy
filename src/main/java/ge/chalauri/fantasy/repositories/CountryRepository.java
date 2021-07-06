package ge.chalauri.fantasy.repositories;


import java.util.List;

import ge.chalauri.fantasy.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    List<Country> findAll();
}
