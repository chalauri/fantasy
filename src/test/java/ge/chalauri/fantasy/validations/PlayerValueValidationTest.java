package ge.chalauri.fantasy.validations;

import java.math.BigDecimal;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlayerValueValidationTest {

    private final PlayerValueValidation validation = new PlayerValueValidation();

    @Test
    public void validateShouldThrowApiExceptionWhenPlayerValueIsZero() {
        // Given
        Player player = Player.builder().price(BigDecimal.ZERO).build();

        // When

        // Then
        assertThrows(ApiException.class, () -> validation.validate(player));
    }

    @Test
    public void validateShouldDoNothingWhenPlayerValueIsMoreThanZero() {
        // Given
        Player player = Player.builder().price(BigDecimal.TEN).build();

        // When
        validation.validate(player);
    }
}
