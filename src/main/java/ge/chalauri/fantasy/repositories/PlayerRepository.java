package ge.chalauri.fantasy.repositories;


import java.math.BigDecimal;
import java.util.List;

import ge.chalauri.fantasy.model.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

    @Query(value = "SELECT e  FROM Player e JOIN FETCH e.country where e.teamId =:teamId ")
    List<Player> findAllByTeamId(@Param("teamId") Integer teamId);

    @Modifying
    @Query(value = "UPDATE Player set teamId =:teamId, price = (price * :increaseRate) WHERE playerId =:playerId")
    void updatePlayer(@Param("teamId") Integer teamId, @Param("increaseRate") BigDecimal increaseRate, @Param("playerId") Integer playerId);

    List<Player> findAll();

    void deleteAllByTeamId(Integer teamId);
}
