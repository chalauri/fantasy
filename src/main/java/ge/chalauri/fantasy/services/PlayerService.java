package ge.chalauri.fantasy.services;

import java.math.BigDecimal;
import java.util.List;

import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.PlayerRepository;
import ge.chalauri.fantasy.repositories.TeamRepository;
import ge.chalauri.fantasy.repositories.TransferRepository;
import ge.chalauri.fantasy.validations.PlayerValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final List<PlayerValidation> validations;
    private final TeamRepository teamRepository;
    private final TransferRepository transferRepository;

    public PlayerService(PlayerRepository playerRepository, List<PlayerValidation> validations, TeamRepository teamRepository,
                         TransferRepository transferRepository) {
        this.playerRepository = playerRepository;
        this.validations = validations;
        this.teamRepository = teamRepository;
        this.transferRepository = transferRepository;
    }

    public List<Player> findAll(Integer teamId) {
        return playerRepository.findAllByTeamId(teamId);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Iterable<Player> saveAll(List<Player> players) {
        return playerRepository.saveAll(players);
    }

    @Transactional
    public Player saveOrUpdate(Player player) {

        for (PlayerValidation validation : validations) {
            validation.validate(player);
        }

        player = playerRepository.save(player);

        updateTeamInfo(player.getTeamId());
        return player;
    }

    @Transactional
    public Integer delete(Integer playerId) {

        transferRepository.deleteAllByPlayerPlayerId(playerId);

        playerRepository.deleteById(playerId);

        return playerId;
    }

    @Transactional
    public void deleteAll(Integer teamId) {
        transferRepository.deleteAllByFromTeamTeamId(teamId);
        transferRepository.deleteAllByToTeamTeamId(teamId);
        playerRepository.deleteAllByTeamId(teamId);
    }

    private void updateTeamInfo(Integer teamId) {
        Team team = teamRepository.findById(teamId).get();
        List<Player> fromTeamPlayers = playerRepository.findAllByTeamId(teamId);
        BigDecimal fromTeamValue = fromTeamPlayers.stream().map(Player::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        team.setTeamValue(fromTeamValue);
        teamRepository.save(team);
    }
}
