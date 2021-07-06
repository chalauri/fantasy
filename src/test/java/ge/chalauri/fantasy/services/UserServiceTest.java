package ge.chalauri.fantasy.services;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.model.response.UserDetailsImpl;
import ge.chalauri.fantasy.repositories.UserRepository;
import ge.chalauri.fantasy.validations.UserRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamService teamService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserRequestValidator userRequestValidator = new UserRequestValidator();
        userService = new UserService(userRepository, teamService, userRequestValidator);
    }

    @Test
    public void saveShouldThrowExceptionWhenEmailIsEmpty() {

        // Given
        User user = new User();

        // Then
        assertThrows(ApiException.class, () -> userService.save(user));
    }

    @Test
    public void saveShouldThrowExceptionWhenPasswordIsEmpty() {

        // Given
        User user = User.builder().email("email").build();

        // Then
        assertThrows(ApiException.class, () -> userService.save(user));
    }

    @Test
    public void saveShouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        User user = User.builder().username("username").email("email").password("password").build();
        User existing = User.builder().username("username").email("email").password("otherPassword").build();

        // When
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existing));

        // Then
        assertThrows(ApiException.class, () -> userService.save(user));
    }

    @Test
    public void saveShouldThrowExceptionWhenEmailExists() {

        // Given
        User user = User.builder().email("email").password("password").build();
        User existing = User.builder().email("email").password("otherPassword").build();

        // When
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(existing));

        // Then
        assertThrows(ApiException.class, () -> userService.save(user));
    }

    @Test
    public void saveShouldCreateAccountAndGenerateTeam() {

        // Given
        User user = User.builder().email("email").password("pass").build();

        // When
        userService.save(user);

        // Then
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail("email");
        verify(teamService).generate(any(User.class));
    }

    @Test
    public void findAllShouldCallRepositoryService() {

        // Given

        // When
        when(userRepository.findAll()).thenReturn(emptyList());
        userService.findAll();

        // Then
        verify(userRepository).findAll();
    }

    @Test
    public void loadUserByUsernameShouldThrowApiExceptionIfUserNotFound() {
        // Given
        String username = "username";

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApiException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWithTeamWhenUserAndTeamExists() {
        // Given
        String username = "username";
        String email = "email@email.com";
        String password = "email@email.com";
        int userId = 1;
        String role = "user";
        User user = buildUser(username, email, password, userId, role);
        UserDetails expected = buildUserDetails(username, email, password, userId, role);

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(teamService.getUserTeam(userId)).thenReturn(Optional.of(new Team()));
        UserDetailsImpl response = (UserDetailsImpl) userService.loadUserByUsername(username);

        // Then
        assertThat(response).isEqualTo(expected);
        assertThat(response.getTeam()).isNotNull();
        verify(teamService).getUserTeam(userId);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWithoutTeamWhenUserDoesNotHaveTeam() {
        // Given
        String username = "username";
        String email = "email@email.com";
        String password = "email@email.com";
        int userId = 1;
        String role = "user";
        User user = buildUser(username, email, password, userId, role);
        UserDetails expected = buildUserDetails(username, email, password, userId, role);

        // When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(teamService.getUserTeam(userId)).thenReturn(Optional.empty());
        UserDetailsImpl response = (UserDetailsImpl) userService.loadUserByUsername(username);

        // Then
        assertThat(response).isEqualTo(expected);
        assertThat(response.getTeam()).isNull();
        verify(teamService).getUserTeam(userId);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void deleteShouldDeleteTeamAndPlayersWhenUserHasTeam() {
        // Given
        Integer userId = 1;
        Team team = Team.builder().teamId(1).build();

        // When
        when(teamService.getUserTeam(userId)).thenReturn(Optional.of(team));
        doNothing().when(teamService).delete(1);
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);

        // Then
        verify(teamService).delete(1);
        verify(teamService).getUserTeam(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void deleteShouldNotCallTeamDeleteMethodWhenUserDoesNotHaveTeam() {
        // Given
        Integer userId = 1;

        // When
        when(teamService.getUserTeam(userId)).thenReturn(Optional.empty());
        doNothing().when(userRepository).deleteById(userId);
        userService.delete(userId);

        // Then
        verify(teamService, never()).delete(1);
        verify(teamService).getUserTeam(userId);
        verify(userRepository).deleteById(userId);
    }

    private User buildUser(String username, String email, String password, int userId, String role) {
        return User.builder()
                   .userId(userId)
                   .username(username)
                   .password(password)
                   .role(role)
                   .email(email).build();
    }

    private UserDetails buildUserDetails(String username, String email, String password, int userId, String role) {
        return new UserDetailsImpl(Long.valueOf(userId), username, email, password, singletonList(new SimpleGrantedAuthority(role)), null);
    }
}
