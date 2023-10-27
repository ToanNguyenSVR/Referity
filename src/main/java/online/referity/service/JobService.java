package online.referity.service;

import online.referity.dto.response.JobReport;

import java.util.UUID;

public interface JobService {

    public JobReport getJobReport(UUID jobId);
}
