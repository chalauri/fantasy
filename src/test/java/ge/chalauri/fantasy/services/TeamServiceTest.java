package ge.chalauri.fantasy.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.common.Position;
import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.repositories.CountryRepository;
import ge.chalauri.fantasy.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private PlayerService playerService;
    @Mock
    private CountryRepository countryRepository;

    private TeamService teamService;

    @BeforeEach
    public void setUp() {
        teamService = new TeamService(teamRepository, playerService,
            countryRepository, Collections.emptyList());
    }

    @Test
    public void generateShouldGenerateTeam() {

        // Given
        User user = new User();

        // When
        when(countryRepository.findAll()).thenReturn(Collections.singletonList(new Country()));
        Team team = teamService.generate(user);
        HashMap<String, List<Player>> playersByPositionsMap = team.getPlayers()
                                                                  .stream()
                                                                  .collect(groupingBy(Player::getPosition, HashMap::new, toList()));

        // Then
        verify(countryRepository).findAll();
        verify(teamRepository).save(any(Team.class));
        verify(playerService).saveAll(anyList());
        assertThat(team).isNotNull();
        assertThat(team.getPlayers()).isNotEmpty();
        assertThat(team.getPlayers()).hasSize(20);
        assertThat(playersByPositionsMap.get(Position.GOALKEEPER.getLabel())).hasSize(3);
        assertThat(playersByPositionsMap.get(Position.DEFENDER.getLabel())).hasSize(6);
        assertThat(playersByPositionsMap.get(Position.MIDFIELDER.getLabel())).hasSize(6);
        assertThat(playersByPositionsMap.get(Position.ATTACKER.getLabel())).hasSize(5);
    }

    @Test
    public void updateShouldCallRepositoryMethod() {
        // Given
        Team team = new Team();

        // When
        when(teamRepository.save(team)).thenReturn(team);
        teamService.update(team);

        // Then
        verify(teamRepository).save(team);
    }

    @Test
    public void getUserTeamShouldCallRepositoryMethodsAndReturnTeamWhenTeamExists() {
        // Given
        String username = "username";
        Integer teamId = 1;
        Team team = Team.builder().username(username).teamId(teamId).build();

        // When
        when(teamRepository.findByUsername(username)).thenReturn(Optional.of(team));
        when(playerService.findAll(teamId)).thenReturn(Collections.singletonList(new Player()));
        Team userTeam = teamService.getUserTeam(username);

        // Then
        assertThat(userTeam).isNotNull();
        assertThat(userTeam.getPlayers()).hasSize(1);
        verify(playerService).findAll(teamId);
        verify(teamRepository).findByUsername(username);
    }

    @Test
    public void getUserTeamShouldThrowApiExceptionWhenTeamDoesNotExist() {
        // Given
        String username = "username";

        // When
        when(teamRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> teamService.getUserTeam(username));
    }

    @Test
    public void getUserTeamByUserIdShouldCallRepositoryMethodsAndReturnTeamOptionalWhenTeamExists() {
        // Given
        Integer userId = 1;
        Integer teamId = 1;
        Team team = Team.builder().teamId(teamId).userId(userId).build();

        // When
        when(teamRepository.findByUserId(userId)).thenReturn(Optional.of(team));
        when(playerService.findAll(teamId)).thenReturn(Collections.singletonList(new Player()));
        Optional<Team> userTeamOptional = teamService.getUserTeam(userId);

        // Then
        assertThat(userTeamOptional).isNotEmpty();
        assertThat(userTeamOptional.get().getPlayers()).hasSize(1);
        verify(playerService).findAll(teamId);
        verify(teamRepository).findByUserId(userId);
    }

    @Test
    public void findByIdShouldCallRepositoryService() {
        // Given
        Integer teamId = 1;

        // When
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(new Team()));
        teamService.findById(teamId);

        // Then
        verify(teamRepository).findById(teamId);
    }

    @Test
    public void getUserTeamByUserIdShouldReturnEmptyOptionalWhenTeamDoesNotExist() {
        // Given
        Integer userId = 1;

        // When
        when(teamRepository.findByUserId(userId)).thenReturn(Optional.empty());
        Optional<Team> userTeamOptional = teamService.getUserTeam(userId);

        // Then
        assertThat(userTeamOptional).isEmpty();
    }

    @Test
    public void updateShouldThrowApiExceptionWhenTeamDoesNotExist() {
        // Given
        Integer teamId = 1;

        // When
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> teamService.update(teamId, "name", new Country(), BigDecimal.TEN));
    }

    @Test
    public void updateShouldThrowApiExceptionWhenTeamBudgetIsNegative() {
        // Given
        Integer teamId = 1;

        // When
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(new Team()));

        // Then
        assertThrows(ApiException.class, () -> teamService.update(teamId, "name", new Country(), BigDecimal.valueOf(-1)));
    }

    @Test
    public void updateShouldCallRepositoryMethodsWhenValidationsPass() {
        // Given
        Integer teamId = 1;

        // When
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(new Team()));
        teamService.update(teamId, "name", new Country(), BigDecimal.TEN);

        // Then
        verify(teamRepository).findById(teamId);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    public void findAllShouldCallRepositoryMethod() {
        // Given

        // When
        when(teamRepository.findAll()).thenReturn(Collections.emptyList());
        teamService.findAll();

        // Then
        verify(teamRepository).findAll();
    }

    @Test
    public void deleteShouldCallRepositoryAndServiceMethods() {
        // Given
        Integer teamId = 1;

        // When
        doNothing().when(teamRepository).deleteById(teamId);
        doNothing().when(playerService).deleteAll(teamId);
        teamService.delete(teamId);

        // Then
        verify(teamRepository).deleteById(teamId);
        verify(playerService).deleteAll(teamId);
    }

    @Test
    public void createShouldCallRepositoryMethod() {
        // Given
        Team team = Team.builder().name("team").build();

        // When
        when(teamRepository.save(team)).thenReturn(team);
        teamService.create(team);

        // Then
        verify(teamRepository).save(team);
    }

}
