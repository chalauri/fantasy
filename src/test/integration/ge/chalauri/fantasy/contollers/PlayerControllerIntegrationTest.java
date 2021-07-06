package ge.chalauri.fantasy.contollers;

import java.math.BigDecimal;
import java.util.List;

import ge.chalauri.fantasy.config.FantasyTestContextConfig;
import ge.chalauri.fantasy.controllers.PlayerController;
import ge.chalauri.fantasy.model.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = FantasyTestContextConfig.class)
public class PlayerControllerIntegrationTest {

    @Autowired
    private PlayerController playerController;

    @Test
    @Sql(scripts = "classpath:db/player/setUpTeamWithOnePlayer.sql")
    public void listShouldReturnAllPlayersFromDatabase() {
        //When
        List<Player> response = playerController.findAll();

        // Then
        assertThat(response).hasSize(1);
    }

    @Test
    @Sql(scripts = "classpath:db/player/setUpTeamWithOnePlayer.sql")
    public void updateShouldUpdatePlayerInformation() {
        // Given
        Player player = Player
            .builder()
            .playerId(1)
            .teamId(1)
            .price(BigDecimal.valueOf(1_000_000))
            .firstName("ChangedFirstName")
            .build();

        //When
        Player response = playerController.update(player);

        // Then
        assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(1_000_000));
        assertThat(response.getFirstName()).isEqualTo("ChangedFirstName");
    }

    @Test
    @Sql(scripts = "classpath:db/player/setUpTeamWithOnePlayer.sql")
    public void deleteShouldPlayerFromDatabase() {
        // Given
        Integer playerId = 1;

        //When
        playerController.delete(playerId);
        List<Player> players = playerController.findAll();

        // Then
        assertThat(players).isEmpty();
    }
}
