package com.jobtracker.user.controller;

import com.jobtracker.user.entity.User;
import com.jobtracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/jobs")
    public Object getAllJobsFromJobService() {
        String url = "http://job/jobs";  // use Job service's spring.application.name
        return restTemplate.getForObject(url, Object.class);
    }


    // Registration endpoint
    @PostMapping
    public User registerUser(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }
        if (service.getUserByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        return service.createUser(user);
    }

    // Login endpoint
    @PostMapping("/login")
    public User loginUser(@RequestBody User loginRequest) {
        User user = service.getUserByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    // Get user by email
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        User user = service.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}