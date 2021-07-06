package ge.chalauri.fantasy.validations;

import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.repositories.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class TeamNameAlreadyUsedValidation implements TeamValidation {

    private final TeamRepository teamRepository;

    public TeamNameAlreadyUsedValidation(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void validate(Team team) {
        Optional<Team> teamOptional = teamRepository.findByName(team.getName());

        if(teamOptional.isPresent()){
            throw new ApiException("Team with this name already exists");
        }

    }
}
