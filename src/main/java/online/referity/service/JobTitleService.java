package online.referity.service;

import online.referity.entity.JobTitle;

import java.util.List;
import java.util.UUID;

public interface JobTitleService {

    public JobTitle createJobTitle(JobTitle position);

    public List<JobTitle> createJobTitle(List<JobTitle> position);

    public List<JobTitle> seachJobTitle(String key);

    public List<JobTitle> getJobTitle();

    public  JobTitle update( JobTitle position , UUID id );
    public  JobTitle delete( UUID id );
}
