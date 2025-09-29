package com.jobtracker.application.controller;
import com.jobtracker.application.entity.Application;
import com.jobtracker.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService service;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/jobs")
    public Object getAllJobs() {
        String url = "http://job/jobs";  // Job microservice endpoint
        return restTemplate.getForObject(url, Object.class);
    }


    @PostMapping
    public Application create(@RequestBody Application application) {
        return service.saveApplication(application);
    }

    @GetMapping
    public List<Application> getAll() {
        return service.getAllApplications();
    }

    @GetMapping("/user/{userId}")
    public List<Application> getByUser(@PathVariable int userId) {
        return service.getApplicationsByUser(userId);
    }

    @GetMapping("/job/{jobId}")
    public List<Application> getByJob(@PathVariable int jobId) {
        return service.getApplicationsByJob(jobId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteApplication(id);
    }
}