package ge.chalauri.fantasy.contollers;

import java.util.List;

import ge.chalauri.fantasy.config.FantasyTestContextConfig;
import ge.chalauri.fantasy.controllers.PlayerController;
import ge.chalauri.fantasy.controllers.TeamController;
import ge.chalauri.fantasy.controllers.UserController;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.User;
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
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private PlayerController playerController;

    @Test
    @Sql(scripts = "classpath:db/player/setUpTeamWithOnePlayer.sql")
    public void deleteShouldDeleteUserIncludingWithTeamAndPlayers() {
        // Given
        Integer userId = 1;

        // When
        userController.delete(userId);
        List<Team> teams = teamController.findAll();
        List<Player> players = playerController.findAll();
        List<User> users = userController.findAll();

        // Then
        assertThat(teams).hasSize(0);
        assertThat(players).hasSize(0);
        assertThat(users).hasSize(0);
    }

}
