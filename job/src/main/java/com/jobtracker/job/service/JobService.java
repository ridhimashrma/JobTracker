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

    // ✅ New method to get applications for a job
    public List<ApplicationDTO> getApplicationsForJob(int jobId) {
        String url = "http://application-service/applications/job/" + jobId;
        ApplicationDTO[] applications = restTemplate.getForObject(url, ApplicationDTO[].class);
        return Arrays.asList(applications);
    }
}
