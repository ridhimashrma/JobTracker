package com.jobtracker.job.entity;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false)
    private String company;

    @Column(name = "role_required", nullable = false)
    private String roleRequired;

    @Column(nullable = false)
    private Date deadline;

    // New field for job required skills (comma-separated values)
    @Column(name = "required_skills")
    private String requiredSkills; // e.g., "Java,Spring Boot,Microservices"

    public Job() {}

    public Job(int id, String title, String description, String company,
               String roleRequired, Date deadline, String requiredSkills) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.roleRequired = roleRequired;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getRoleRequired() { return roleRequired; }
    public void setRoleRequired(String roleRequired) { this.roleRequired = roleRequired; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", company='" + company + '\'' +
                ", roleRequired='" + roleRequired + '\'' +
                ", deadline=" + deadline +
                ", requiredSkills='" + requiredSkills + '\'' +
                '}';
    }
}
