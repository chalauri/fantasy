package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class CanNotBuyOwnPlayerValidation implements BuyPlayerValidation {

    @Override
    public void validate(Transfer transfer, Team userTeam) {

        if(userTeam.getTeamId().equals(transfer.getFromTeam().getTeamId())){
            throw new ApiException("You can't buy your own player");
        }
    }
}
