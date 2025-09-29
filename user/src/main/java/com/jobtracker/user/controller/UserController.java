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

    // Registration
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        if (user.getName() == null || user.getEmail() == null ||
                user.getPassword() == null || user.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }
        if (service.getUserByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // Skills are optional at registration, but we still accept them
        return service.createUser(user);
    }


    // Login
    @PostMapping("/login")
    public User loginUser(@RequestBody User loginRequest) {
        User user = service.getUserByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            service.setLoggedIn(user.getEmail(), true); // set login status
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }
    @GetMapping("/isLoggedIn")
    public Boolean checkLoggedIn(@RequestParam String email) {
        return service.isLoggedIn(email);
    }

    @GetMapping("/checkLogin")
    public boolean checkLogin(@RequestParam String email) {
        return service.isLoggedIn(email);
    }
    // Logout
    @PostMapping("/logout")
    public String logoutUser(@RequestParam String email) {
        service.setLoggedIn(email, false);
        return "Logged out successfully";
    }

    // Get all users (protected)
    @GetMapping
    public List<User> getAllUsers(@RequestParam String email) {
        if (!service.isLoggedIn(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return service.getAllUsers();
    }

    // Get user by email (protected)
    @GetMapping("/{userEmail}")
    public User getUserByEmail(@PathVariable String userEmail, @RequestParam String email) {
        if (!service.isLoggedIn(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        User user = service.getUserByEmail(userEmail);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user;
    }

    // Fetch all jobs from Job service (protected)
    @GetMapping("/jobs")
    public Object getAllJobs(@RequestParam String email) {
        if (!service.isLoggedIn(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        String url = "http://job/jobs"; // Job microservice endpoint
        return restTemplate.getForObject(url, Object.class);
    }

    //Update user profile (for updating skills, etc.)
    @PutMapping("/{userEmail}")
    public User updateUser(@PathVariable String userEmail, @RequestBody User updatedUser, @RequestParam String email) {
        if (!service.isLoggedIn(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        User existingUser = service.getUserByEmail(userEmail);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // update fields if provided
        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
        if (updatedUser.getSkills() != null) existingUser.setSkills(updatedUser.getSkills());

        return service.createUser(existingUser); // reuse save method
    }
}
