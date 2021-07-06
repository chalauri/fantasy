package ge.chalauri.fantasy.controllers;

import java.math.BigDecimal;
import java.util.Collections;

import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import ge.chalauri.fantasy.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    private TransferController transferController;

    @BeforeEach
    public void setUp() {
        transferController = new TransferController(transferService);
    }

    @Test
    public void putOnTransferShouldCallTransferServiceWhenDoneByUser() {

        // Given
        Transfer transfer = buildTransfer();

        // When
        when(transferService.putOnTransfer(transfer, false)).thenReturn(transfer);
        transferController.putOnTransfer(transfer);

        // Then
        verify(transferService).putOnTransfer(transfer, false);
    }

    @Test
    public void putOnTransferShouldCallTransferServiceWhenDoneByAdmin() {

        // Given
        Transfer transfer = buildTransfer();

        // When
        when(transferService.putOnTransfer(transfer, true)).thenReturn(transfer);
        transferController.putOnTransferByAdmin(transfer);

        // Then
        verify(transferService).putOnTransfer(transfer, true);
    }

    @Test
    public void searchShouldCallService() {

        // Given
        TransferSearch search = new TransferSearch();

        // When
        when(transferService.search(search)).thenReturn(Collections.emptyList());
        transferController.search(search);

        // Then
        verify(transferService).search(search);
    }

    @Test
    public void buyShouldCallServiceMethod() {
        // Given
        Integer transferId = 1;

        // When
        when(transferService.buy(transferId)).thenReturn(new Transfer());
        transferController.buy(transferId);

        // Then
        verify(transferService).buy(transferId);
    }

    private Transfer buildTransfer() {
        return Transfer
            .builder()
            .fromTeam(Team.builder().teamId(1).build())
            .toTeam(Team.builder().teamId(2).build())
            .player(Player.builder().playerId(1).build())
            .transferPrice(BigDecimal.TEN)
            .build();
    }
}
