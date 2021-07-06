package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;

public interface BuyPlayerValidation {

    void validate(Transfer transfer, Team userTeam);
}
