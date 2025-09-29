package com.jobtracker.job.controller;

import com.jobtracker.job.entity.Job;
import com.jobtracker.job.service.JobService;
import com.jobtracker.job.dto.ApplicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user/{userId}")
    public Object getUserDetails(@PathVariable int userId) {
        String url = "http://user/users/" + userId;
        return restTemplate.getForObject(url, Object.class);
    }


    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.saveJob(job);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobsSortedByTitle();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable int id) {
        return jobService.findById(id);
    }

    @GetMapping("/search")
    public List<Job> searchJobs(@RequestParam String title) {
        return jobService.findByTitle(title);
    }

    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable int id) {
        jobService.deleteJobById(id);
        return "Job deleted successfully!";
    }
    @GetMapping("/{id}/applications")
    public List<ApplicationDTO> getApplicationsForJob(@PathVariable int id) {
        return jobService.getApplicationsForJob(id);
    }

}