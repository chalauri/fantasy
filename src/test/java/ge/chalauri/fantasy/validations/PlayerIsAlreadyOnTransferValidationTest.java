package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.repositories.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlayerIsAlreadyOnTransferValidationTest {

    @Mock
    private TransferRepository transferRepository;

    PlayerIsAlreadyOnTransferValidation validation;

    @BeforeEach
    public void setUp() {
        validation = new PlayerIsAlreadyOnTransferValidation(transferRepository);
    }

    @Test
    public void validateShouldThrowApiExceptionWhenPlayerIsAlreadyOnTransfer() {
        // Given
        Integer playerId = 1;
        Transfer transfer = buildTransfer(playerId);

        // When
        Mockito.when(transferRepository.findByPlayerPlayerIdAndBought(playerId, false)).thenReturn(Optional.of(transfer));

        // Then
        assertThrows(ApiException.class, () -> validation.validate(transfer, null));
    }

    @Test
    public void validateShouldDoNothingWhenPlayerIsNotOnTransfer() {
        // Given
        Integer playerId = 1;
        Transfer transfer = buildTransfer(playerId);

        // When
        Mockito.when(transferRepository.findByPlayerPlayerIdAndBought(playerId, false)).thenReturn(Optional.empty());
        validation.validate(transfer, null);
    }

    private Transfer buildTransfer(Integer playerId) {
        return Transfer
            .builder()
            .player(buildPlayer(playerId))
            .build();
    }

    private Player buildPlayer(Integer playerId) {
        return Player
            .builder()
            .playerId(playerId)
            .build();
    }
}
