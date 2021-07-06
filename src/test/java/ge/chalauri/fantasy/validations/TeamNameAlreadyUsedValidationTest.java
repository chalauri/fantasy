package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamNameAlreadyUsedValidationTest {

    @Mock
    private TeamRepository teamRepository;

    private TeamNameAlreadyUsedValidation validation;

    @BeforeEach
    public void setUp() {
        validation = new TeamNameAlreadyUsedValidation(teamRepository);
    }

    @Test
    public void validateShouldThrowApiExceptionWhenTeamWithProvidedNameAlreadyExists() {
        // Given
        Team team = Team.builder().name("team").build();

        // When
        when(teamRepository.findByName("team")).thenReturn(Optional.of(team));

        // Then
        assertThrows(ApiException.class, () -> validation.validate(team));
    }

    @Test
    public void validateShouldDoNothingWhenTeamWithProvidedNameDoesNotExist() {
        // Given
        Team team = Team.builder().name("team").build();

        // When
        when(teamRepository.findByName("team")).thenReturn(Optional.empty());
        validation.validate(team);

        // Then
        verify(teamRepository).findByName("team");
    }

}
