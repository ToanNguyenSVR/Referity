package online.referity.service.imp;

import online.referity.dto.request.RecruitStageRequest;
import online.referity.entity.RecruitStage;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.RecruitStageRepository;
import online.referity.service.RecruitStageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecruitStageServiceImp  implements RecruitStageService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    RecruitStageRepository recruitStageRepository;
    @Override
    public RecruitStage createProcess(RecruitStageRequest request) {
       RecruitStage recruitStage = modelMapper.map(request, RecruitStage.class);
       return recruitStageRepository.save(recruitStage);
    }

    @Override
    public RecruitStage update(RecruitStageRequest request, UUID stageId) {
        Optional<RecruitStage> recruitStage = recruitStageRepository.findById(stageId);
        if(!recruitStage.isPresent()) throw  new EntityNotFound("Recruit stage not found with id : " + stageId);
        modelMapper.map(request,recruitStage.get());
        return recruitStageRepository.save(recruitStage.get());
    }

    @Override
    public RecruitStage deleted( UUID stageId) {
        Optional<RecruitStage> recruitStage = recruitStageRepository.findById(stageId);
        if(!recruitStage.isPresent()) throw  new EntityNotFound("Recruit stage not found with id : " + stageId);
        recruitStage.get().setIsDeleted(true);
        return recruitStageRepository.save(recruitStage.get());
    }

    @Override
    public List<RecruitStage> get() {
        return recruitStageRepository.findRecruiterStagesByIsDeletedFalse().stream().sorted(Comparator.comparing(RecruitStage::getNoOfStage)).collect(Collectors.toList());
    }
}
