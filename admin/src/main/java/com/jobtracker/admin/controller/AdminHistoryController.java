package com.jobtracker.admin.controller;

import com.jobtracker.admin.entity.AdminHistoryEntry;
import com.jobtracker.admin.service.AdminHistoryEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminHistoryController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AdminHistoryEntryService historyService;

    // ------------------ Helper ------------------
    private void ensureAdminLoggedIn(String adminEmail) {
        if (!"admin@jobtracker.com".equals(adminEmail)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin login required");
        }
    }

    // ------------------ Users ------------------
    @GetMapping("/users")
    public Object getAllUsers(@RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return restTemplate.getForObject("http://USER-SERVICE/users", Object.class);
    }

    // ------------------ Jobs ------------------
    @GetMapping("/jobs")
    public Object getAllJobs(@RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return restTemplate.getForObject("http://JOB-SERVICE/jobs", Object.class);
    }

    @PostMapping("/jobs")
    public Object createJob(@RequestBody Object job, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return restTemplate.postForObject("http://JOB-SERVICE/jobs", job, Object.class);
    }

    @DeleteMapping("/jobs/{id}")
    public void deleteJob(@PathVariable int id, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        restTemplate.delete("http://JOB-SERVICE/jobs/" + id);
    }

    // ------------------ Applications ------------------
    @GetMapping("/applications")
    public Object getAllApplications(@RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return restTemplate.getForObject("http://APPLICATION-SERVICE/applications", Object.class);
    }

    // ------------------ Admin History ------------------
    @GetMapping("/history")
    public ResponseEntity<List<AdminHistoryEntry>> getHistory(@RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return ResponseEntity.ok(historyService.findAll());
    }

    @GetMapping("/history/user/{userName}")
    public ResponseEntity<List<AdminHistoryEntry>> getByUser(@PathVariable String userName, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return ResponseEntity.ok(historyService.findByUserName(userName));
    }

    @GetMapping("/history/company/{companyName}")
    public ResponseEntity<List<AdminHistoryEntry>> getByCompany(@PathVariable String companyName, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return ResponseEntity.ok(historyService.findByCompanyName(companyName));
    }

    @GetMapping("/history/status/{status}")
    public ResponseEntity<List<AdminHistoryEntry>> getByStatus(@PathVariable String status, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return ResponseEntity.ok(historyService.findByStatusKeyword(status));
    }

    @PostMapping("/history")
    public ResponseEntity<AdminHistoryEntry> addHistory(@RequestBody AdminHistoryEntry entry, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        return ResponseEntity.ok(historyService.saveEntry(entry));
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id, @RequestParam String adminEmail) {
        ensureAdminLoggedIn(adminEmail);
        historyService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
