package ge.chalauri.fantasy.contollers;

import java.util.List;
import java.util.stream.Collectors;

import ge.chalauri.fantasy.common.Position;
import ge.chalauri.fantasy.config.FantasyTestContextConfig;
import ge.chalauri.fantasy.controllers.SignUpController;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.model.request.SignupRequest;
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
public class SignUpControllerIntegrationTest {

    @Autowired
    private SignUpController controller;

    @Test
    @Sql(scripts = "classpath:db/country/insertCountries.sql")
    public void signUpShouldCreateUserWhenRoleIsNotPassedAndGenerateTeam() {
        // Given
        SignupRequest signupRequest = buildSignUpRequest(null);

        // When
        User user = controller.signUp(signupRequest);

        // Then
        List<Player> players = user.getTeam().getPlayers();
        assertThat(user.getRole()).isEqualTo("USER");
        assertThat(user.getTeam()).isNotNull();
        assertThat(players).hasSize(20);
        assertThat(getPlayers(players, Position.GOALKEEPER)).hasSize(3);
        assertThat(getPlayers(players, Position.DEFENDER)).hasSize(6);
        assertThat(getPlayers(players, Position.MIDFIELDER)).hasSize(6);
        assertThat(getPlayers(players, Position.ATTACKER)).hasSize(5);
    }

    @Test
    @Sql(scripts = "classpath:db/country/insertCountries.sql")
    public void signUpShouldCreateUserWhenRoleIsUserPassedAndGenerateTeam() {
        // Given
        SignupRequest signupRequest = buildSignUpRequest("user");

        // When
        User user = controller.signUp(signupRequest);

        // Then
        List<Player> players = user.getTeam().getPlayers();
        assertThat(user.getRole()).isEqualTo("USER");
        assertThat(user.getTeam()).isNotNull();
        assertThat(players).hasSize(20);
        assertThat(getPlayers(players, Position.GOALKEEPER)).hasSize(3);
        assertThat(getPlayers(players, Position.DEFENDER)).hasSize(6);
        assertThat(getPlayers(players, Position.MIDFIELDER)).hasSize(6);
        assertThat(getPlayers(players, Position.ATTACKER)).hasSize(5);
    }

    @Test
    @Sql(scripts = "classpath:db/country/insertCountries.sql")
    public void signUpShouldCreateUserWhenRoleIsAdminPassedAndGenerateTeam() {
        // Given
        SignupRequest signupRequest = buildSignUpRequest("admin");

        // When
        User user = controller.signUp(signupRequest);

        // Then
        List<Player> players = user.getTeam().getPlayers();
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.getTeam()).isNotNull();
        assertThat(players).hasSize(20);
        assertThat(getPlayers(players, Position.GOALKEEPER)).hasSize(3);
        assertThat(getPlayers(players, Position.DEFENDER)).hasSize(6);
        assertThat(getPlayers(players, Position.MIDFIELDER)).hasSize(6);
        assertThat(getPlayers(players, Position.ATTACKER)).hasSize(5);
    }

    private List<Player> getPlayers(List<Player> players, Position position) {
        return players.stream().filter(player -> player.getPosition().equals(position.getLabel())).collect(Collectors.toList());
    }

    private SignupRequest buildSignUpRequest(String role) {
        return SignupRequest
            .builder()
            .email("email@gmail.com")
            .password("password")
            .username("username")
            .role(role)
            .build();
    }
}
