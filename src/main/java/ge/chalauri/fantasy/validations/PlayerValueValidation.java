package ge.chalauri.fantasy.validations;

import java.math.BigDecimal;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerValueValidation implements PlayerValidation {


    @Override
    public void validate(Player player) {
        if (player.getPrice().compareTo(BigDecimal.ZERO) < 1) {
            throw new ApiException("Player value should be more than zero ");
        }
    }
}
