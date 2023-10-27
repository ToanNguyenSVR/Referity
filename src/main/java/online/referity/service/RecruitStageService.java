package online.referity.service;

import online.referity.dto.request.RecruitStageRequest;
import online.referity.entity.RecruitStage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface RecruitStageService {

    public RecruitStage createProcess ( RecruitStageRequest request );
    public RecruitStage update ( RecruitStageRequest request , UUID stageId );
    public RecruitStage deleted (UUID stageId );
    public List<RecruitStage> get();



}
