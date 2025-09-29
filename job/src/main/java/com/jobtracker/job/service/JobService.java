package com.jobtracker.job.service;

import com.jobtracker.job.entity.Job;
import com.jobtracker.job.repository.JobRepository;
import com.jobtracker.job.dto.ApplicationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Arrays;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getAllJobsSortedByTitle() {
        return jobRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Job> findByTitle(String title) {
        return jobRepository.findByTitleIgnoreCase(title);
    }

    public void deleteJobById(int id) {
        jobRepository.deleteById(id);
    }

    public Job findById(int id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id " + id));
    }

    // Get applications for a job via Application service
    public List<ApplicationDTO> getApplicationsForJob(int jobId) {
        String url = "http://APPLICATION-SERVICE/applications/job/" + jobId;
        ApplicationDTO[] applications = restTemplate.getForObject(url, ApplicationDTO[].class);
        return Arrays.asList(applications);
    }

    // Recommend jobs for a user based on their skills
    public List<Job> recommendJobsForUser(String userEmail) {
        // 1. Fetch user details from USER-SERVICE
        String userUrl = "http://USER-SERVICE/users/" + userEmail + "?email=" + userEmail;
        var user = restTemplate.getForObject(userUrl, com.jobtracker.job.dto.UserDTO.class);

        if (user == null || user.getSkills() == null || user.getSkills().isEmpty()) {
            throw new RuntimeException("User or skills not found for email: " + userEmail);
        }

        // 2. Get all jobs
        List<Job> allJobs = jobRepository.findAll();

        // 3. Match skills with job roles
        String[] userSkills = user.getSkills().toLowerCase().split(",");
        return allJobs.stream()
                .filter(job -> {
                    String role = job.getRoleRequired().toLowerCase();
                    return Arrays.stream(userSkills).anyMatch(skill -> role.contains(skill.trim()));
                })
                .toList();
    }
}
