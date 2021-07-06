package ge.chalauri.fantasy.controllers;

import java.util.Collections;

import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    private PlayerController playerController;

    @BeforeEach
    public void setUp() {
        playerController = new PlayerController(playerService);
    }

    @Test
    public void findAllShouldCallServiceMethod() {

        // Given

        // When
        when(playerService.findAll()).thenReturn(Collections.emptyList());
        playerController.findAll();

        // Then
        verify(playerService).findAll();
    }

    @Test
    public void updateShouldCallServiceMethod() {

        // Given
        Player player = new Player();

        // When
        when(playerService.saveOrUpdate(player)).thenReturn(player);
        playerController.update(player);

        // Then
        verify(playerService).saveOrUpdate(player);
    }

    @Test
    public void deleteShouldCallServiceMethod() {

        // Given
        Integer playerId = 1;

        // When
        when(playerService.delete(playerId)).thenReturn(playerId);
        playerController.delete(playerId);

        // Then
        verify(playerService).delete(playerId);
    }

}
