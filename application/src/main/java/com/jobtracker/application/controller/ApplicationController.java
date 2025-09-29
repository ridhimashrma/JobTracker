package com.jobtracker.application.controller;

import com.jobtracker.application.entity.Application;
import com.jobtracker.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService service;

    @Autowired
    private RestTemplate restTemplate; // @LoadBalanced bean in config

    private final String USER_SERVICE_NAME = "USER-SERVICE"; // Eureka service name

    // ------------------ Helper ------------------
    private void checkUserLogin(String email) {
        String loginCheckUrl = "http://" + USER_SERVICE_NAME + "/users/isLoggedIn?email=" + email;
        Boolean isLoggedIn = restTemplate.getForObject(loginCheckUrl, Boolean.class);
        if (isLoggedIn == null || !isLoggedIn) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
    }

    // Return type is Long, and assignment is corrected. URL typo fixed.
    private Long getUserIdByEmail(String email) {
        String url = "http://" + USER_SERVICE_NAME + "/users/idByEmail?email=" + email;
        // The return type of restTemplate.getForObject must match the method's return type (Long)
        Long userId = restTemplate.getForObject(url, Long.class);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userId;
    }

    // ------------------ User Operations ------------------
    @PostMapping
    public Application createApplication(@RequestBody Application application, @RequestParam String email) {
        checkUserLogin(email);
        Long userId = getUserIdByEmail(email); // Type is Long
        application.setUserId(userId);
        return service.saveApplication(application);
    }

    @GetMapping
    public List<Application> getAllApplications(@RequestParam String email) {
        checkUserLogin(email);
        Long userId = getUserIdByEmail(email); // Type is Long
        return service.getApplicationsByUser(userId);
    }

    @DeleteMapping("/{id}")
    // Application ID should be Long to match the service/database
    public void deleteApplication(@PathVariable Long id, @RequestParam String email) {
        checkUserLogin(email);
        Long userId = getUserIdByEmail(email);
        Application app = service.getApplicationById(id);
        if (app == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        if (!app.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete others' applications");
        }
        service.deleteApplication(id);
    }

    // ------------------ Admin Operations ------------------
    @GetMapping("/all")
    public List<Application> getAllApplicationsForAdmin(@RequestParam String adminEmail) {
        if (!"admin@jobtracker.com".equals(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin login required");
        }
        return service.getAllApplications();
    }

    @GetMapping("/job/{jobId}")
    // The path variable type should match the service method signature (Long)
    public List<Application> getApplicationsByJob(@PathVariable Long jobId, @RequestParam String adminEmail) {
        if (!"admin@jobtracker.com".equals(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin login required");
        }
        return service.getApplicationsByJob(jobId);
    }
}