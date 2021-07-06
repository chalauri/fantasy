package ge.chalauri.fantasy.model.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferSearch {

    private String country;
    private String playerName;
    private String teamName;
    private BigDecimal value;
}
