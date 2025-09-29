package com.jobtracker.job.controller;

import com.jobtracker.job.entity.Job;
import com.jobtracker.job.service.JobService;
import com.jobtracker.job.dto.ApplicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private RestTemplate restTemplate;

    // ------------------ Helper ------------------
    private void checkUserLogin(String email) {
        String url = "http://USER-SERVICE/users/isLoggedIn?email=" + email;
        Boolean loggedIn = restTemplate.getForObject(url, Boolean.class);
        if (loggedIn == null || !loggedIn) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Login required");
        }
    }

    // ------------------ Job Operations ------------------
    @PostMapping
    public Job createJob(@RequestBody Job job, @RequestParam String email) {
        checkUserLogin(email);
        return jobService.saveJob(job);
    }

    @GetMapping
    public List<Job> getAllJobs(@RequestParam String email) {
        checkUserLogin(email);
        return jobService.getAllJobsSortedByTitle();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable int id, @RequestParam String email) {
        checkUserLogin(email);
        return jobService.findById(id);
    }

    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String title, @RequestParam String email) {
        checkUserLogin(email);
        return jobService.findByTitle(title);
    }

    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable int id, @RequestParam String email) {
        checkUserLogin(email);
        jobService.deleteJobById(id);
        return "Job deleted successfully!";
    }

    @GetMapping("/{id}/applications")
    public List<ApplicationDTO> getApplicationsForJob(@PathVariable int id, @RequestParam String email) {
        checkUserLogin(email);
        return jobService.getApplicationsForJob(id);
    }
}
