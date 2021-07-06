package ge.chalauri.fantasy.services;

import java.math.BigDecimal;
import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import ge.chalauri.fantasy.repositories.PlayerRepository;
import ge.chalauri.fantasy.repositories.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;
    @Mock
    private TeamService teamService;
    @Mock
    private PlayerRepository playerRepository;

    private TransferService transferService;

    @BeforeEach
    public void setUp() {
        transferService = new TransferService(transferRepository, emptyList(), emptyList(), teamService, playerRepository);
    }

    @Test
    public void putOnTransferShouldCallRepositoryMethodWhenCalledByUser() {
        // Given
        Transfer transfer = new Transfer();

        // When
        when(transferRepository.save(transfer)).thenReturn(transfer);
        transferService.putOnTransfer(transfer, false);

        // Then
        verify(transferRepository).save(transfer);
    }

    @Test
    public void putOnTransferShouldCallRepositoryMethodWhenCalledByAdmin() {
        // Given
        Integer teamId = 1;
        Transfer transfer = buildTransfer();

        // When
        when(transferRepository.save(transfer)).thenReturn(transfer);
        when(teamService.findById(teamId)).thenReturn(Optional.of(Team.builder().teamId(teamId).build()));
        transferService.putOnTransfer(transfer, true);

        // Then
        verify(transferRepository).save(transfer);
        verify(teamService).findById(teamId);
    }

    @Test
    public void putOnTransferShouldThrowApiExceptionWhenTeamDoesNotExistAndMethodCalledByAdmin() {
        // Given
        Integer teamId = 1;
        Transfer transfer = buildTransfer();

        // When
        when(teamService.findById(teamId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> transferService.putOnTransfer(transfer, true));
    }

    @Test
    public void searchShouldCallRepositoryMethod() {
        // Given
        TransferSearch transferSearch = new TransferSearch();

        // When
        when(transferRepository.findAll(any(Specification.class))).thenReturn(emptyList());
        transferService.search(transferSearch);

        // Then
        verify(transferRepository).findAll(any(Specification.class));
    }

    @Test
    public void buyShouldThrowApiExceptionWhenIllegalTransferIdIsProvided() {
        // Given
        Integer transferId = 1;

        // When
        when(transferRepository.findById(transferId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> transferService.buy(transferId));
    }

    @Test
    public void buyShouldCallRepositoryMethods() {
        // Given
        Integer transferId = 1;
        Team userTeam = Team.builder().budget(BigDecimal.valueOf(100_000_000)).teamId(1).build();
        Team fromTeam = Team.builder().teamId(2).build();
        Player player = Player.builder().playerId(1).build();
        Transfer transfer = buildTransfer(transferId, fromTeam, player);

        // When
        when(transferRepository.findById(transferId)).thenReturn(Optional.of(transfer));
        when(teamService.getUserTeam(anyString())).thenReturn(userTeam);
        when(transferRepository.save(transfer)).thenReturn(transfer);
        doNothing().when(playerRepository).updatePlayer(anyInt(), any(BigDecimal.class), anyInt());
        when(playerRepository.findAllByTeamId(userTeam.getTeamId())).thenReturn(emptyList());
        when(playerRepository.findAllByTeamId(fromTeam.getTeamId())).thenReturn(emptyList());
        doNothing().when(teamService).update(userTeam);
        doNothing().when(teamService).update(fromTeam);

        transferService.buy(transferId);

        // Then
        verify(transferRepository).findById(transferId);
        verify(teamService).getUserTeam(anyString());
        verify(transferRepository).save(transfer);
        verify(playerRepository).updatePlayer(anyInt(), any(BigDecimal.class), anyInt());
        verify(playerRepository).findAllByTeamId(1);
        verify(teamService).update(fromTeam);
        verify(teamService).update(userTeam);
    }

    private Transfer buildTransfer(Integer transferId, Team fromTeam, Player player) {
        return Transfer
            .builder()
            .fromTeam(fromTeam)
            .transferId(transferId)
            .player(player)
            .transferPrice(BigDecimal.TEN)
            .build();
    }

    private Transfer buildTransfer() {
        return Transfer
            .builder()
            .fromTeam(Team.builder().teamId(1).build())
            .transferPrice(BigDecimal.TEN)
            .build();
    }
}