package ge.chalauri.fantasy.controllers;

import java.util.Collections;

import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService, encoder);
    }

    @Test
    public void findAllShouldCallServiceMethod() {

        // Given

        // When
        when(userService.findAll()).thenReturn(Collections.emptyList());
        userController.findAll();

        // Then
        verify(userService).findAll();
    }

    @Test
    public void deleteAllShouldCallServiceMethod() {

        // Given
        Integer userId = 1;

        // When
        when(userService.delete(userId)).thenReturn(userId);
        userController.delete(userId);

        // Then
        verify(userService).delete(userId);
    }

    @Test
    public void createShouldCallServiceMethod() {

        // Given
        User user = User.builder().username("username").password("password").role("user").build();

        // When
        when(userService.save(user)).thenReturn(user);
        userController.create(user);

        // Then
        verify(userService).save(user);
    }
}
