package ge.chalauri.fantasy.contollers;

import java.math.BigDecimal;
import java.util.List;

import ge.chalauri.fantasy.config.FantasyTestContextConfig;
import ge.chalauri.fantasy.controllers.TransferController;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = FantasyTestContextConfig.class)
public class TransferControllerIntegrationTest {

    @Autowired
    private TransferController transferController;

    @Test
    @Sql(scripts = "classpath:db/transfer/setUpPlayerForPuttingOnTransfer.sql")
    public void putOnTransferShouldPutPlayerOnTransfer() {
        // Given
        Transfer transfer = Transfer
            .builder()
            .transferPrice(BigDecimal.valueOf(100000))
            .player(Player.builder().playerId(1).build())
            .build();

        // When
        Transfer playerOnTransfer = transferController.putOnTransfer(transfer);

        // Then
        assertThat(playerOnTransfer.getTransferId()).isNotNull();
    }

    @Test
    @Sql(scripts = "classpath:db/transfer/playersOnTransfer.sql")
    public void searchShouldReturnPlayersOnTransferBasedOnCriteria() {
        // Given
        TransferSearch transferSearch = TransferSearch
            .builder()
            .country("Georgia")
            .teamName("TEAM")
            .playerName("Giga")
            .value(BigDecimal.valueOf(1000000))
            .build();

        // When
        List<Transfer> transfers = transferController.search(transferSearch);

        // Then
        assertThat(transfers).hasSize(1);
    }
}
