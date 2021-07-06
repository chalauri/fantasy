package ge.chalauri.fantasy.repositories;

import java.util.Optional;

import ge.chalauri.fantasy.model.Transfer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer>, JpaSpecificationExecutor<Transfer> {

    Optional<Transfer> findByPlayerPlayerIdAndBought(Integer playerId, boolean bought);

    void deleteAllByPlayerPlayerId(Integer playerId);

    void deleteAllByFromTeamTeamId(Integer teamId);

    void deleteAllByToTeamTeamId(Integer teamId);
}
