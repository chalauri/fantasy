package ge.chalauri.fantasy.controllers;

import java.math.BigDecimal;
import java.util.Collections;

import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    private TeamController teamController;

    @BeforeEach
    public void setUp() {
        teamController = new TeamController(teamService);
    }

    @Test
    public void updateShouldCallTransferService() {

        // Given
        Integer teamId = 1;
        String teamName = "newName";
        BigDecimal budget = BigDecimal.TEN;
        Country country = new Country();

        // When
        when(teamService.update(teamId, teamName, country, budget)).thenReturn(new Team());
        teamController.update(teamId, country, budget, teamName);

        // Then
        verify(teamService).update(teamId, teamName, country, budget);
    }

    @Test
    public void findAllShouldCallServiceMethod() {
        // Given

        // When
        when(teamService.findAll()).thenReturn(Collections.emptyList());
        teamController.findAll();

        // Then
        verify(teamService).findAll();
    }

    @Test
    public void createShouldCallServiceMethod() {
        // Given
        Team team = Team.builder().name("team").build();

        // When
        when(teamService.create(team)).thenReturn(team);
        teamController.create(team);

        // Then
        verify(teamService).create(team);
    }

    @Test
    public void deleteShouldCallServiceMethod() {
        // Given
        Integer teamId = 1;

        // When
        doNothing().when(teamService).delete(teamId);
        teamController.delete(teamId);

        // Then
        verify(teamService).delete(teamId);
    }
}
