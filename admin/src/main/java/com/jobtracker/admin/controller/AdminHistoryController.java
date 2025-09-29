package com.jobtracker.admin.controller;

import com.jobtracker.admin.entity.AdminHistoryEntry;
import com.jobtracker.admin.service.AdminHistoryEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminHistoryController {

    @Autowired
    private AdminHistoryEntryService historyService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users")
    public Object getAllUsers() {
        String url = "http://user/users";  // User microservice
        return restTemplate.getForObject(url, Object.class);
    }


    // Get all admin history entries
    @GetMapping("/history")
    public ResponseEntity<List<AdminHistoryEntry>> getHistory() {
        return ResponseEntity.ok(historyService.findAll());
    }

    // Search by username
    @GetMapping("/history/user/{userName}")
    public ResponseEntity<List<AdminHistoryEntry>> getByUser(@PathVariable String userName) {
        return ResponseEntity.ok(historyService.findByUserName(userName));
    }

    // Search by company
    @GetMapping("/history/company/{companyName}")
    public ResponseEntity<List<AdminHistoryEntry>> getByCompany(@PathVariable String companyName) {
        return ResponseEntity.ok(historyService.findByCompanyName(companyName));
    }

    // Search by status
    @GetMapping("/history/status/{status}")
    public ResponseEntity<List<AdminHistoryEntry>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(historyService.findByStatusKeyword(status));
    }

    // Add history entry
    @PostMapping("/history")
    public ResponseEntity<AdminHistoryEntry> addHistory(@RequestBody AdminHistoryEntry entry) {
        return ResponseEntity.ok(historyService.saveEntry(entry));
    }

    // Delete history entry
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        historyService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
