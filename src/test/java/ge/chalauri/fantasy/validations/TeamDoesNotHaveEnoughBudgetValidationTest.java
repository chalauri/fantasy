package ge.chalauri.fantasy.validations;

import java.math.BigDecimal;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TeamDoesNotHaveEnoughBudgetValidationTest {

    private final TeamDoesNotHaveEnoughBudgetValidation validation = new TeamDoesNotHaveEnoughBudgetValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenTeamDoesNotHaveEnoughBudget() {
        // Given
        Transfer transfer = buildTransfer(BigDecimal.TEN);
        Team team = buildUserTeam(BigDecimal.ONE);

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(transfer, team));
    }

    @Test
    public void validateShouldDoNothingWhenTeamHasEnoughBudget() {
        // Given
        Transfer transfer = buildTransfer(BigDecimal.ONE);
        Team team = buildUserTeam(BigDecimal.TEN);

        // When
        validation.validate(transfer, team);
    }

    private Transfer buildTransfer(BigDecimal transferPrice) {
        return Transfer
            .builder()
            .transferPrice(transferPrice)
            .build();
    }

    private Team buildUserTeam(BigDecimal budget) {
        return Team
            .builder()
            .budget(budget)
            .build();
    }

}
