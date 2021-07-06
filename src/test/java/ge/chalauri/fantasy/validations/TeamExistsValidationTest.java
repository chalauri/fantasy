package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamExistsValidationTest {

    @Mock
    private TeamRepository teamRepository;

    private TeamExistsValidation validation;

    @BeforeEach
    public void init() {
        validation = new TeamExistsValidation(teamRepository);
    }

    @Test
    public void validateShouldThrowApiExceptionWhenTeamDoesNotExist() {
        // Given
        Integer teamId = 1;
        Player player = Player.builder().teamId(teamId).build();

        // When
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> validation.validate(player));
    }

    @Test
    public void validateShouldDoNothingWhenTeamExists() {
        // Given
        Integer teamId = 1;
        Player player = Player.builder().teamId(teamId).build();

        // When
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(new Team()));
        validation.validate(player);

        // Then
        verify(teamRepository).findById(teamId);
    }

}
