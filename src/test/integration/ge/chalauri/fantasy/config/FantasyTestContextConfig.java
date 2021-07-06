package ge.chalauri.fantasy.config;

import java.util.Collections;
import java.util.List;

import ge.chalauri.fantasy.controllers.PlayerController;
import ge.chalauri.fantasy.controllers.SignUpController;
import ge.chalauri.fantasy.controllers.TeamController;
import ge.chalauri.fantasy.controllers.TransferController;
import ge.chalauri.fantasy.controllers.UserController;
import ge.chalauri.fantasy.repositories.CountryRepository;
import ge.chalauri.fantasy.repositories.PlayerRepository;
import ge.chalauri.fantasy.repositories.TeamRepository;
import ge.chalauri.fantasy.repositories.TransferRepository;
import ge.chalauri.fantasy.repositories.UserRepository;
import ge.chalauri.fantasy.services.PlayerService;
import ge.chalauri.fantasy.services.TeamService;
import ge.chalauri.fantasy.services.TransferService;
import ge.chalauri.fantasy.services.UserService;
import ge.chalauri.fantasy.validations.BelongsToTeamValidation;
import ge.chalauri.fantasy.validations.PlayerIsAlreadyOnTransferValidation;
import ge.chalauri.fantasy.validations.PutOnTransferValidation;
import ge.chalauri.fantasy.validations.UserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
public class FantasyTestContextConfig {

    @Autowired
   private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public UserController userController() {
        return new UserController(userService(), bCryptPasswordEncoder);
    }

    @Bean
    public TeamController teamController(){
        return new TeamController(teamService());
    }

    @Bean
    public TransferController transferController() {
        return new TransferController(transferService());
    }

    @Bean
    public PlayerController playerController() {
        return new PlayerController(playerService());
    }

    @Bean
    public SignUpController signUpController() {
        return new SignUpController(userService(), bCryptPasswordEncoder);
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerService(playerRepository, Collections.emptyList(), teamRepository, transferRepository);
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository, teamService(), userRequestValidator());
    }

    @Bean
    public TeamService teamService() {
        return new TeamService(teamRepository, playerService(), countryRepository, Collections.emptyList());
    }

    @Bean
    public UserRequestValidator userRequestValidator() {
        return new UserRequestValidator();
    }

    @Bean
    public List<PutOnTransferValidation> putOnTransferValidations() {
        return List.of(belongsToTeamValidation(), playerIsAlreadyOnTransferValidation());
    }

    @Bean
    public BelongsToTeamValidation belongsToTeamValidation() {
        return new BelongsToTeamValidation();
    }

    @Bean
    public PlayerIsAlreadyOnTransferValidation playerIsAlreadyOnTransferValidation() {
        return new PlayerIsAlreadyOnTransferValidation(transferRepository);
    }

    @Bean
    public TransferService transferService() {
        return new TransferService(transferRepository, putOnTransferValidations(), Collections.emptyList(), teamService(), playerRepository);
    }
}
