package ge.chalauri.fantasy.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import ge.chalauri.fantasy.repositories.PlayerRepository;
import ge.chalauri.fantasy.repositories.TransferRepository;
import ge.chalauri.fantasy.specifications.SearchTransferSpecification;
import ge.chalauri.fantasy.validations.BuyPlayerValidation;
import ge.chalauri.fantasy.validations.PutOnTransferValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ge.chalauri.fantasy.security.utils.SecurityConstants.LOGGED_IN_USERNAME;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final List<PutOnTransferValidation> putOnTransferValidations;
    private final List<BuyPlayerValidation> buyPlayerValidations;
    private final TeamService teamService;
    private final PlayerRepository playerRepository;

    public TransferService(TransferRepository transferRepository, List<PutOnTransferValidation> putOnTransferValidations,
                           List<BuyPlayerValidation> buyPlayerValidations, TeamService teamService,
                           PlayerRepository playerRepository) {

        this.transferRepository = transferRepository;
        this.putOnTransferValidations = putOnTransferValidations;
        this.buyPlayerValidations = buyPlayerValidations;
        this.teamService = teamService;
        this.playerRepository = playerRepository;
    }

    public Transfer putOnTransfer(Transfer transfer, boolean admin) {
        Team team;
        if (admin) {
            Optional<Team> teamOptional = teamService.findById(transfer.getFromTeam().getTeamId());
            if (teamOptional.isEmpty()) {
                throw new ApiException("Team with provided id does not exist");
            }
            team = teamOptional.get();
        } else {
            team = teamService.getUserTeam(LOGGED_IN_USERNAME);
            transfer.setFromTeam(team);
        }

        putOnTransferValidations.forEach(validation -> validation.validate(transfer, team));

        transfer.setBought(false);

        return transferRepository.save(transfer);
    }

    @Transactional
    public Transfer buy(Integer transferId) {
        Optional<Transfer> transferOptional = transferRepository.findById(transferId);

        if (transferOptional.isEmpty()) {
            throw new ApiException("Transfer with provided id does not exist");
        }

        Team userTeam = teamService.getUserTeam(LOGGED_IN_USERNAME);

        Transfer transfer = transferOptional.get();
        buyPlayerValidations.forEach(validation -> validation.validate(transfer, userTeam));

        updateTransferInfo(userTeam, transfer);

        updatePlayerInfo(userTeam, transfer);

        updateToTeamInfo(userTeam, transfer);

        updateFromTeamInfo(transfer);

        return transfer;
    }

    public List<Transfer> search(TransferSearch transferSearch) {
        return transferRepository.findAll(SearchTransferSpecification.create(transferSearch));
    }

    private void updateTransferInfo(Team userTeam, Transfer transfer) {
        transfer.setBought(true);
        transfer.setBoughtAt(LocalDateTime.now());
        transfer.setToTeam(userTeam);
        transferRepository.save(transfer);
    }

    private void updatePlayerInfo(Team userTeam, Transfer transfer) {
        Random random = new Random();
        BigDecimal increaseRate = BigDecimal.valueOf(((random.nextInt(91) + 10) / (double) 100) + 1);
        playerRepository.updatePlayer(userTeam.getTeamId(), increaseRate, transfer.getPlayer().getPlayerId());
    }

    private void updateToTeamInfo(Team userTeam, Transfer transfer) {
        userTeam.setBudget(userTeam.getBudget().subtract(transfer.getTransferPrice()));
        List<Player> teamPlayers = playerRepository.findAllByTeamId(userTeam.getTeamId());
        BigDecimal teamValue = teamPlayers.stream().map(Player::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        userTeam.setTeamValue(teamValue);
        teamService.update(userTeam);
    }

    private void updateFromTeamInfo(Transfer transfer) {
        Team fromTeam = transfer.getFromTeam();
        List<Player> fromTeamPlayers = playerRepository.findAllByTeamId(fromTeam.getTeamId());
        BigDecimal fromTeamValue = fromTeamPlayers.stream().map(Player::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        fromTeam.setTeamValue(fromTeamValue);
        teamService.update(fromTeam);
    }
}
