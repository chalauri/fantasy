package ge.chalauri.fantasy.validations;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlayerAlreadyBoughtValidationTest {

    private final PlayerIsAlreadyBoughtValidation validation = new PlayerIsAlreadyBoughtValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenPlayerIsAreadyBought() {
        // Given
        Transfer transfer = buildTransfer(true);

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(transfer, null));
    }

    @Test
    public void validateShouldDoNothingWhenPlayerIsStillNotBought() {
        // Given
        Transfer transfer = buildTransfer(false);

        // When
        validation.validate(transfer, null);
    }

    private Transfer buildTransfer(boolean bought) {
        return Transfer
            .builder()
            .bought(bought)
            .build();
    }


}
