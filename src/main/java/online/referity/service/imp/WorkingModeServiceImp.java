package online.referity.service.imp;

import online.referity.entity.WorkingMode;
import online.referity.exception.exceptions.BadRequest;
import online.referity.repository.WorkingModeRepository;
import online.referity.service.WorkingModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkingModeServiceImp implements WorkingModeService {

    @Autowired
    WorkingModeRepository workingModeRepository ;



    @Override
    public WorkingMode create(WorkingMode mode) {
        WorkingMode workingMode = new WorkingMode();
        workingMode.setMode(mode.getMode());
        return workingModeRepository.save(workingMode);
    }

    @Override
    public WorkingMode update(WorkingMode mode, UUID id) {
        Optional<WorkingMode> workingMode = workingModeRepository.findById(id);
        if(!workingMode.isPresent()) throw  new BadRequest("Working mode not found ");
        workingMode.get().setMode(mode.getMode());
        return workingModeRepository.save(workingMode.get());
    }

    @Override
    public WorkingMode delete(UUID id) {
        Optional<WorkingMode> workingMode = workingModeRepository.findById(id);
        if(!workingMode.isPresent()) throw  new BadRequest("Working mode not found ");
        workingMode.get().setDelete(true);
        return workingModeRepository.save(workingMode.get());
    }

    @Override
    public List<WorkingMode> get() {
        return workingModeRepository.findAll();
    }
}

