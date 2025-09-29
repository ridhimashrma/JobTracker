package com.jobtracker.admin.repository;
import com.jobtracker.admin.entity.AdminHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminHistoryEntryRepository extends JpaRepository<AdminHistoryEntry, Long> {

    List<AdminHistoryEntry> findByUserName(String userName);
    List<AdminHistoryEntry> findByCompanyName(String companyName);
    List<AdminHistoryEntry> findByStatusContainingIgnoreCase(String statusKeyword);
}