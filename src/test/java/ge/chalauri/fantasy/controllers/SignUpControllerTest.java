package ge.chalauri.fantasy.controllers;

import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.model.request.SignupRequest;
import ge.chalauri.fantasy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignUpControllerTest {

    @Mock
    private UserService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private SignUpController signUpController;

    @BeforeEach
    public void setUp() {
        signUpController = new SignUpController(userService, encoder);
    }

    @Test
    public void signUpShouldReturnResponseCodeOkWithUserRole() {

        // Given
        SignupRequest signUpRequest = buildSignUpRequest("user");

        // When
        when(userService.save(any(User.class))).thenReturn(new User());
        signUpController.signUp(signUpRequest);

        // Then
        verify(userService).save(any(User.class));
    }

    @Test
    public void signUpShouldReturnResponseCodeOkWithAdminRole() {

        // Given
        SignupRequest signUpRequest = buildSignUpRequest("admin");

        // When
        when(userService.save(any(User.class))).thenReturn(new User());
        signUpController.signUp(signUpRequest);

        // Then
        verify(userService).save(any(User.class));
    }

    @Test
    public void signUpShouldReturnResponseCodeOkWithoutRole() {
        // Given
        SignupRequest signUpRequest = buildSignUpRequest(null);

        // When
        when(userService.save(any(User.class))).thenReturn(new User());
        signUpController.signUp(signUpRequest);

        // Then
        verify(userService).save(any(User.class));
    }

    private SignupRequest buildSignUpRequest(String role) {
        return SignupRequest
            .builder()
            .username("user")
            .email("user@email.com")
            .password("password")
            .role(role)
            .build();
    }

}
