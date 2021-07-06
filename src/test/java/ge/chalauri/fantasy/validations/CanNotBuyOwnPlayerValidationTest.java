package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CanNotBuyOwnPlayerValidationTest {

   private final CanNotBuyOwnPlayerValidation validation = new CanNotBuyOwnPlayerValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenUserTriesToBuyOwnPlayer() {
        // Given
        Transfer transfer = buildTransfer(1);
        Team team = buildUserTeam();

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(transfer, team));
    }

    @Test
    public void validateShouldDoNothingWhenUserTriesToBuyPlayerFromOtherTeam() {
        // Given
        Transfer transfer = buildTransfer(2);
        Team team = buildUserTeam();

        // When
        validation.validate(transfer, team);
    }

    private Transfer buildTransfer(Integer teamId) {
        return Transfer
            .builder()
            .fromTeam(Team.builder().teamId(teamId).build())
            .build();
    }

    private Team buildUserTeam() {
        return Team
            .builder()
            .teamId(1)
            .build();
    }

}
