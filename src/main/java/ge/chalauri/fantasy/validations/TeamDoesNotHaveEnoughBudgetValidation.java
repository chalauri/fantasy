package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class TeamDoesNotHaveEnoughBudgetValidation implements BuyPlayerValidation {

    @Override
    public void validate(Transfer transfer, Team userTeam) {

        if (userTeam.getBudget().compareTo(transfer.getTransferPrice()) < 0) {
            throw new ApiException("You don't have enough budget to buy this player");
        }

    }
}
