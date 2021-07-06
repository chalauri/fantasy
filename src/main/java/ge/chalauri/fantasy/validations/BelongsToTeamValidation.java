package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class BelongsToTeamValidation implements PutOnTransferValidation {

    @Override
    public void validate(Transfer transfer, Team userTeam) {

        Integer playerId = transfer.getPlayer().getPlayerId();

        long count = userTeam.getPlayers().stream().filter(player -> player.getPlayerId().equals(playerId)).count();

        if (count == 0) {
            throw new ApiException("You can only put your players on transfer.");
        }

    }
}
