package com.jobtracker.job.dto;
import java.sql.Date;

public class ApplicationDTO {
    private int id;
    private int jobId;
    private int userId;
    private String status;
    private Date appliedOn;

    public ApplicationDTO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getAppliedOn() { return appliedOn; }
    public void setAppliedOn(Date appliedOn) { this.appliedOn = appliedOn; }
}