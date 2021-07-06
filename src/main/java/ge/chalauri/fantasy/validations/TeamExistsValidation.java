package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamExistsValidation implements PlayerValidation {

    private final TeamRepository teamRepository;

    public TeamExistsValidation(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void validate(Player player) {
        Optional<Team> teamOptional = teamRepository.findById(player.getTeamId());

        if (teamOptional.isEmpty()) {
            throw new ApiException("Tem with provided id does not exists ");
        }
    }
}
