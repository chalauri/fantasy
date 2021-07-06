package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.repositories.TransferRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerIsAlreadyOnTransferValidation implements PutOnTransferValidation {

    private final TransferRepository transferRepository;

    public PlayerIsAlreadyOnTransferValidation(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public void validate(Transfer transfer, Team userTeam) {

        Integer playerId = transfer.getPlayer().getPlayerId();

        Optional<Transfer> playerOnTransferOptional = transferRepository.findByPlayerPlayerIdAndBought(playerId, false);

        if (playerOnTransferOptional.isPresent()) {
            throw new ApiException("Player is already on transfer list");
        }

    }
}
