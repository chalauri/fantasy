package ge.chalauri.fantasy.validations;

import java.math.BigDecimal;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BudgetShouldNotBeNegativeValidationTest {

    private final BudgetShouldNotBeNegativeValidation validation = new BudgetShouldNotBeNegativeValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenTeamBudgetIsLessThanZero() {
        // Given
        Team team = Team.builder().name("team").budget(BigDecimal.valueOf(-100)).build();

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(team));
    }

    @Test
    public void validateShouldDoNothingWhenTeamBudgetIsZero() {
        // Given
        Team team = Team.builder().name("team").budget(BigDecimal.ZERO).build();

        // When
        validation.validate(team);
    }

    @Test
    public void validateShouldDoNothingWhenTeamBudgetIsMoreThanZero() {
        // Given
        Team team = Team.builder().name("team").budget(BigDecimal.TEN).build();

        // When
        validation.validate(team);
    }

}
