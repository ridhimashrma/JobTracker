package com.jobtracker.application.service;

import com.jobtracker.application.dto.JobDTO;
import com.jobtracker.application.dto.UserDTO;
import com.jobtracker.application.entity.Application;
import com.jobtracker.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://USER-SERVICE/users/";
    private final String JOB_SERVICE_URL = "http://JOB-SERVICE/jobs/";

    public Application saveApplication(Application application) {

        // fetch user
        ResponseEntity<UserDTO> userRes =
                restTemplate.getForEntity(USER_SERVICE_URL + application.getUserId(), UserDTO.class);
        if (userRes.getStatusCode().is2xxSuccessful() && userRes.getBody() != null) {
            application.setUserName(userRes.getBody().getName());
        }

        // fetch job
        ResponseEntity<JobDTO> jobRes =
                restTemplate.getForEntity(JOB_SERVICE_URL + application.getJobId(), JobDTO.class);
        if (jobRes.getStatusCode().is2xxSuccessful() && jobRes.getBody() != null) {
            application.setJobTitle(jobRes.getBody().getTitle());
            application.setCompany(jobRes.getBody().getCompany());
        }

        if (application.getStatus() == null) {
            application.setStatus("Applied");
        }

        return repository.save(application);
    }

    public List<Application> getAllApplications() {
        return repository.findAll();
    }

    public List<Application> getApplicationsByUser(int userId) {
        return repository.findByUserId(userId);
    }

    public List<Application> getApplicationsByJob(int jobId) {
        return repository.findByJobId(jobId);
    }

    public void deleteApplication(int id) {
        repository.deleteById(id);
    }
}
