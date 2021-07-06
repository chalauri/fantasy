package ge.chalauri.fantasy.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.PlayerRepository;
import ge.chalauri.fantasy.repositories.TeamRepository;
import ge.chalauri.fantasy.repositories.TransferRepository;
import ge.chalauri.fantasy.validations.PlayerValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TransferRepository transferRepository;

    @Mock
    private PlayerValidation validation;

    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerService = new PlayerService(playerRepository, Collections.singletonList(validation), teamRepository, transferRepository);
    }

    @Test
    public void findAllByTeamIdShouldCallRepositoryService() {

        // Given
        Integer teamId = 1;

        // When
        when(playerRepository.findAllByTeamId(teamId)).thenReturn(Collections.emptyList());
        playerService.findAll(teamId);

        // Then
        verify(playerRepository).findAllByTeamId(teamId);
    }

    @Test
    public void findAllShouldCallRepositoryService() {

        // Given

        // When
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());
        playerService.findAll();

        // Then
        verify(playerRepository).findAll();
    }

    @Test
    public void saveAllShouldCallRepositoryMethod() {

        // Given
        List<Player> players = Collections.singletonList(new Player());

        // When
        when(playerRepository.saveAll(players)).thenReturn(players);
        playerService.saveAll(players);

        // Then
        verify(playerRepository).saveAll(players);
    }

    @Test
    public void saveOrUpdateShouldCallRepositoryMethods() {
        // Given
        Integer teamId = 1;
        Player player = Player.builder().teamId(teamId).build();
        Team team = Team.builder().teamId(teamId).build();

        // When
        when(playerRepository.save(player)).thenReturn(player);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(playerRepository.findAllByTeamId(teamId)).thenReturn(Collections.emptyList());
        doNothing().when(validation).validate(player);
        playerService.saveOrUpdate(player);

        // Then
        verify(playerRepository).save(player);
        verify(teamRepository).save(team);
        verify(teamRepository).findById(teamId);
    }

    @Test
    public void deleteShouldCallRepositoryMethod() {

        // Given
        Integer playerId = 1;

        // When
        doNothing().when(playerRepository).deleteById(playerId);
        doNothing().when(transferRepository).deleteAllByPlayerPlayerId(playerId);
        playerService.delete(playerId);

        // Then
        verify(playerRepository).deleteById(playerId);
        verify(transferRepository).deleteAllByPlayerPlayerId(playerId);
    }

}
