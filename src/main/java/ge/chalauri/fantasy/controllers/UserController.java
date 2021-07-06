package ge.chalauri.fantasy.controllers;

import java.util.List;

import ge.chalauri.fantasy.aspect.annotations.HasRole;
import ge.chalauri.fantasy.model.User;
import ge.chalauri.fantasy.security.utils.Role;
import ge.chalauri.fantasy.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/user")
@RestController
public class UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;

    public UserController(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/list")
    @HasRole(Role.ADMIN)
    public List<User> findAll() {

        return userService.findAll();
    }

    @DeleteMapping("/delete/{userId}")
    @HasRole(Role.ADMIN)
    public Integer delete(@PathVariable("userId") Integer userId) {

        return userService.delete(userId);
    }

    @PostMapping("/create")
    @HasRole(Role.ADMIN)
    public User create(@RequestBody User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        return userService.save(user);
    }
}
