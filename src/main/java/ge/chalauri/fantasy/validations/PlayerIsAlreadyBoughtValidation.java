package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public class PlayerIsAlreadyBoughtValidation implements BuyPlayerValidation {

    @Override
    public void validate(Transfer transfer, Team userTeam) {

        if(transfer.isBought()){
            throw new ApiException("PLayer is already bought");
        }

    }
}
