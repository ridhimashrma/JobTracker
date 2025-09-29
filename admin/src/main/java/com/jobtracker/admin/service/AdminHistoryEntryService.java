package com.jobtracker.admin.service;

import com.jobtracker.admin.entity.AdminHistoryEntry;
import com.jobtracker.admin.repository.AdminHistoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminHistoryEntryService {

    @Autowired
    private AdminHistoryEntryRepository repository;

    public AdminHistoryEntry saveEntry(AdminHistoryEntry entry) {
        return repository.save(entry);
    }

    public List<AdminHistoryEntry> findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    public List<AdminHistoryEntry> findByCompanyName(String companyName) {
        return repository.findByCompanyName(companyName);
    }

    public List<AdminHistoryEntry> findByStatusKeyword(String keyword) {
        return repository.findByStatusContainingIgnoreCase(keyword);
    }

    public List<AdminHistoryEntry> findAll() {
        return repository.findAll();
    }

    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }
}
