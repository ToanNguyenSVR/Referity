package online.referity.service.imp;

import online.referity.entity.JobTitle;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.JobTitleRepository;
import online.referity.service.JobTitleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobTitleServiceImp implements JobTitleService
{
@Autowired
    JobTitleRepository jobTitleRepository ;

ModelMapper modelMapper = new ModelMapper();
    @Override
    public JobTitle createJobTitle(JobTitle position) {
        JobTitle jobTitle = new JobTitle();
        modelMapper.map(position , jobTitle);
        return jobTitleRepository.save(jobTitle) ;
    }

    @Override
    public List<JobTitle> createJobTitle(List<JobTitle> position) {
        List<JobTitle> list = new ArrayList<>();
        for (JobTitle jobTitle: position ) {
            modelMapper.map(position , jobTitle);
            list.add(jobTitle);
        }
        return  jobTitleRepository.saveAll(list);
    }

    @Override
    public List<JobTitle> seachJobTitle(String key) {
        return jobTitleRepository.seach(key);
    }

    @Override
    public List<JobTitle> getJobTitle() {
        return jobTitleRepository.findAll().stream().filter(jobTitle -> !jobTitle.isDelete()).collect(Collectors.toList());
    }

    @Override
    public JobTitle update(JobTitle position, UUID id) {
       JobTitle jobTitle = jobTitleRepository.getById(id);
//        if(!jobTitle.isPresent()) throw  new EntityNotFound("JobTitle Not found ");
        jobTitle.setPosition(position.getPosition());
        return jobTitleRepository.save(jobTitle);
    }

    @Override
    public JobTitle delete(UUID id) {
        Optional<JobTitle> jobTitle = jobTitleRepository.findById(id);
        if(!jobTitle.isPresent()) throw  new EntityNotFound("JobTitle Not found ");
        jobTitle.get().setDelete(true);
        return jobTitleRepository.save(jobTitle.get());
    }
}
