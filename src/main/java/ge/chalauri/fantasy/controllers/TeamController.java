package ge.chalauri.fantasy.controllers;

import java.math.BigDecimal;
import java.util.List;

import ge.chalauri.fantasy.aspect.annotations.HasRole;
import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.security.utils.Role;
import ge.chalauri.fantasy.services.TeamService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/team")
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PutMapping("/update/{teamId}")
    @HasRole(Role.ADMIN)
    public Team update(@PathVariable("teamId") Integer teamId,
                       @RequestParam("country") Country country,
                       @RequestParam("budget") BigDecimal budget,
                       @RequestParam("name") String name) {

        return teamService.update(teamId, name, country, budget);
    }

    @GetMapping("/list")
    @HasRole(Role.ADMIN)
    public List<Team> findAll() {

        return teamService.findAll();
    }

    @PostMapping("/create")
    @HasRole(Role.ADMIN)
    public Team create(@RequestBody Team team) {
        return teamService.create(team);
    }

    @DeleteMapping("/delete/{teamId}")
    @HasRole(Role.ADMIN)
    public void delete(@PathVariable("teamId") Integer teamId) {
        teamService.delete(teamId);
    }
}
