package ge.chalauri.fantasy.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ge.chalauri.fantasy.exceptions.ApiException;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.model.response.UserDetailsImpl;
import ge.chalauri.fantasy.repositories.UserRepository;
import ge.chalauri.fantasy.validations.UserRequestValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TeamService teamService;
    private final UserRequestValidator userRequestValidator;

    public UserService(UserRepository userRepository, TeamService teamService,
                       UserRequestValidator userRequestValidator) {
        this.userRepository = userRepository;
        this.teamService = teamService;
        this.userRequestValidator = userRequestValidator;
    }

    public User save(User user) {
        userRequestValidator.validate(user);

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        userOptional.ifPresent(value -> {
            throw new ApiException("User with provided email address already exists");
        });

        userOptional = userRepository.findByEmail(user.getEmail());

        userOptional.ifPresent(value -> {
            throw new ApiException("username is already in use");
        });

        userRepository.save(user);

        Team userTeam = teamService.generate(user);
        user.setTeam(userTeam);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Team> userTeamOptional = teamService.getUserTeam(user.getUserId());

            return userTeamOptional.map(team -> new UserDetailsImpl(Long.valueOf(user.getUserId()), user.getUsername(), user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())), team))
                                   .orElseGet(() -> new UserDetailsImpl(Long.valueOf(user.getUserId()), user.getUsername(), user.getEmail(), user.getPassword(),
                                       Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))));

        }

        throw new ApiException("User not found");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public Integer delete(Integer userId) {
        Optional<Team> userTeam = teamService.getUserTeam(userId);
        if (userTeam.isPresent()) {
            Team team = userTeam.get();
            teamService.delete(team.getTeamId());
        }
        userRepository.deleteById(userId);

        return userId;
    }

}
