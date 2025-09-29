package com.jobtracker.application.service;

import com.jobtracker.application.dto.JobDTO;
import com.jobtracker.application.dto.UserDTO;
import com.jobtracker.application.entity.Application;
import com.jobtracker.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException; // Import ResponseStatusException

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_NAME = "USER-SERVICE";
    private final String JOB_SERVICE_NAME = "JOB-SERVICE";

    public Application saveApplication(Application application) {
        UserDTO user = null;
        JobDTO job = null;

        // 1. Fetch User and enforce required data
        try {
            ResponseEntity<UserDTO> userRes = restTemplate.getForEntity(
                    "http://" + USER_SERVICE_NAME + "/users/" + application.getUserId(), UserDTO.class);

            if (userRes.getStatusCode() == HttpStatus.OK && userRes.getBody() != null) {
                user = userRes.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + application.getUserId() + " not found or service error.");
            }
        } catch (Exception e) {
            // Catch RestTemplate connection/timeout issues
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to connect to USER-SERVICE.", e);
        }

        // 2. Fetch Job and enforce required data
        try {
            ResponseEntity<JobDTO> jobRes = restTemplate.getForEntity(
                    "http://" + JOB_SERVICE_NAME + "/jobs/" + application.getJobId(), JobDTO.class);

            if (jobRes.getStatusCode() == HttpStatus.OK && jobRes.getBody() != null) {
                job = jobRes.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job with ID " + application.getJobId() + " not found or service error.");
            }
        } catch (Exception e) {
            // Catch RestTemplate connection/timeout issues
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to connect to JOB-SERVICE.", e);
        }

        // 3. Set derived fields (now guaranteed to have user and job objects)
        application.setUserName(user.getName());
        application.setJobTitle(job.getTitle());
        application.setCompany(job.getCompany());

        if (application.getStatus() == null) {
            application.setStatus("Applied");
        }

        return repository.save(application);
    }

    // ... rest of the service methods remain the same ...
    // Note: You must ensure your controller calls still align with Long IDs
    // (e.g., getApplicationsByJob and deleteApplication need to accept Long)

    public List<Application> getAllApplications() {
        return repository.findAll();
    }

    public Application getApplicationById(Long id) {
        Optional<Application> optionalApp = repository.findById(id);
        return optionalApp.orElse(null);
    }

    public List<Application> getApplicationsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return repository.findByJobId(jobId);
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id);
    }
}