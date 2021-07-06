package ge.chalauri.fantasy.validations;

import java.math.BigDecimal;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import org.springframework.stereotype.Component;

@Component
public class BudgetShouldNotBeNegativeValidation implements TeamValidation {

    @Override
    public void validate(Team team) {

        if (team.getBudget().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException("Team budget should not be negative value");
        }

    }
}
