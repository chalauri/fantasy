package ge.chalauri.fantasy.controllers;

import java.util.List;

import ge.chalauri.fantasy.aspect.annotations.HasRole;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.security.utils.Role;
import ge.chalauri.fantasy.services.PlayerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/player")
@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/list")
    @HasRole(Role.ADMIN)
    public List<Player> findAll() {

        return playerService.findAll();
    }

    @PutMapping("/update")
    @HasRole(Role.ADMIN)
    public Player update(Player player) {

        return playerService.saveOrUpdate(player);
    }

    @DeleteMapping("/delete/{playerId}")
    @HasRole(Role.ADMIN)
    public Integer delete(@PathVariable("playerId") Integer playerId) {

        return playerService.delete(playerId);
    }

}
