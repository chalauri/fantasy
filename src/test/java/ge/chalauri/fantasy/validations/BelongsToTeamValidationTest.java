package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BelongsToTeamValidationTest {

    private final BelongsToTeamValidation validation = new BelongsToTeamValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenPlayerDoesNotBelongToUserTeam() {
        // Given
        Transfer transfer = buildTransfer();
        Team team = buildUserTeam(2);

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(transfer, team));
    }

    @Test
    public void validateShouldDoNothingWhenPlayerBelongsToUserTeam() {
        // Given
        Transfer transfer = buildTransfer();
        Team team = buildUserTeam(1);

        // When
        validation.validate(transfer, team);
    }

    private Transfer buildTransfer() {
        return Transfer
            .builder()
            .player(buildPlayer(1))
            .build();
    }

    private Team buildUserTeam(Integer playerId) {
        return Team
            .builder()
            .players(singletonList(buildPlayer(playerId)))
            .build();
    }

    private Player buildPlayer(Integer playerId) {
        return Player
            .builder()
            .playerId(playerId)
            .build();
    }
}
