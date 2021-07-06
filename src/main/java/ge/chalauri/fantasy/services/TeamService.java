package ge.chalauri.fantasy.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.TeamWithoutPlayers;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.repositories.CountryRepository;
import ge.chalauri.fantasy.repositories.TeamRepository;
import ge.chalauri.fantasy.utils.TeamGenerator;
import ge.chalauri.fantasy.validations.TeamValidation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final CountryRepository countryRepository;
    private final List<TeamValidation> teamValidations;

    public TeamService(TeamRepository teamRepository, PlayerService playerService,
                       CountryRepository countryRepository, List<TeamValidation> teamValidations) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
        this.countryRepository = countryRepository;
        this.teamValidations = teamValidations;
    }

    public Team generate(User user) {
        List<Country> countries = countryRepository.findAll();

        Team team = TeamGenerator.generateTeam(countries);
        team.setUserId(user.getUserId());
        team.setUsername(user.getUsername());

        teamRepository.save(team);

        team.getPlayers().forEach(player -> player.setTeamId(copy(team).getTeamId()));
        playerService.saveAll(team.getPlayers());

        return team;
    }

    public Optional<Team> findById(Integer teamId) {
        return teamRepository.findById(teamId);
    }

    public Optional<Team> getUserTeam(Integer userId) {
        Optional<Team> teamOptional = teamRepository.findByUserId(userId);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            List<Player> players = playerService.findAll(team.getTeamId());
            team.setPlayers(players);
            return Optional.of(team);
        }

        return teamOptional;
    }

    public Team getUserTeam(String username) {

        Optional<Team> teamOptional = teamRepository.findByUsername(username);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            List<Player> players = playerService.findAll(team.getTeamId());
            team.setPlayers(players);
            return team;
        }

        throw new ApiException("User does not have a team");
    }

    public void update(Team userTeam) {
        teamRepository.save(userTeam);
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team update(Integer teamId, String name, Country country, BigDecimal budget) {

        Optional<Team> teamOptional = teamRepository.findById(teamId);

        if (teamOptional.isEmpty()) {
            throw new ApiException("Team with provided id does not exist");
        }

        Team team = teamOptional.get();

        updateTeamInfo(name, country, budget, team);

        return teamRepository.save(team);
    }

    @Transactional
    public void delete(Integer teamId) {
        playerService.deleteAll(teamId);
        teamRepository.deleteById(teamId);
    }

    private void updateTeamInfo(String name, Country country, BigDecimal budget, Team team) {
        if (StringUtils.isNotBlank(name)) {
            team.setName(name);
        }

        if (country != null) {
            team.setCountry(country);
        }

        if (budget != null) {
            if (budget.compareTo(BigDecimal.ZERO) < 0) {
                throw new ApiException("Budget cannot be negative");
            }

            team.setBudget(budget);
        }
    }

    private TeamWithoutPlayers copy(Team team) {
        return TeamWithoutPlayers
            .builder()
            .teamId(team.getTeamId())
            .build();
    }

    public Team create(Team team) {
        teamValidations.forEach(validation -> validation.validate(team));

        return teamRepository.save(team);
    }
}
