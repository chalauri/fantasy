package ge.chalauri.fantasy.controllers;

import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.model.request.SignupRequest;
import ge.chalauri.fantasy.security.utils.Role;
import ge.chalauri.fantasy.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/signup")
@RestController
public class SignUpController {

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    public SignUpController(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping
    public User signUp(@RequestBody SignupRequest signUpRequest) {

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

        String role;

        if (signUpRequest.getRole() == null) {
            role = Role.USER.name();
        } else {
            if ("admin".equals(signUpRequest.getRole())) {
                role = Role.ADMIN.name();
            } else {
                role = Role.USER.name();
            }
        }

        user.setRole(role);
        user = userService.save(user);

        return user;
    }

}
