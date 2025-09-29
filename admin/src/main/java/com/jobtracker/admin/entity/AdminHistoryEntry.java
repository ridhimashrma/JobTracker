package com.jobtracker.admin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_history_entries")
public class AdminHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String status;

    public AdminHistoryEntry() {}

    public AdminHistoryEntry(String userName, String jobTitle, String companyName, String status) {
        this.userName = userName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
    }

    // getters & setters

    @Override
    public String toString() {
        return "User: " + userName + ", Job: " + jobTitle + ", Company: " + companyName + ", Status: " + status;
    }
}