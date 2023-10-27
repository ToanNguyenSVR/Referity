package online.referity.service.imp;


import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.SkillListRequest;
import online.referity.dto.response.CandidateApplyReport;
import online.referity.dto.response.JobReport;
import online.referity.dto.response.TransactionResponse;
import online.referity.entity.*;
import online.referity.enums.JobStageStatus;
import online.referity.enums.ResultStage;
import online.referity.enums.TransactionType;
import online.referity.exception.exceptions.BadRequest;
import online.referity.repository.JobRepository;
import online.referity.repository.RatingRepository;
import online.referity.service.JobService;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class JobServiceImp implements JobService
{

    @Autowired
    JobRepository jobRepository ;
    @Autowired
    RatingRepository ratingRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public JobReport getJobReport(UUID jobId) {
        long startTime = System.currentTimeMillis();

        Job job = jobRepository.findJobById(jobId);
        if(job == null ) throw new BadRequest("Bad Request ");
        JobReport result = convertToJobResponse(job);
        List<CandidateApplyReport> candidateApplies = new ArrayList<>() ;
        if(job.getCandidateApplies().size() > 0) {
            job.getCandidateApplies().forEach(candidateApply ->
            {
                CandidateApplyReport candidateApplyReport = new CandidateApplyReport();
                switch (candidateApply.getResultApply()) {
                    case FAIL:
                        result.setCandidateFail(result.getCandidateFail() + 1);
                        break;
                    case IN_PROCESS:
                        result.setCandidateInProcess(result.getCandidateInProcess() + 1);
                        break;
                    case PASS:
                        result.setCandidatePass(result.getCandidatePass() + 1);
                        break;

                }
                candidateApplyReport.setComment(candidateApply.getNote());
                candidateApplyReport.setHeadhunterName(candidateApply.getCvShared().getHeadhunter().getAccount().getFullName());
                if( ratingRepository.findByCandidateApplyId(candidateApply.getId()).isEmpty() ) candidateApplyReport.setRated(false);
                candidateApplyReport.setCandidateApplyId(candidateApply.getId());
                candidateApplyReport.setCandidateAvatar(candidateApply.getCvShared().getCv().getCandidate().getAccount().getAvatar());
                candidateApplyReport.setCandidateName(candidateApply.getCvShared().getCv().getFullName());
                candidateApplyReport.setStatus(candidateApply.getResultApply());
                if(candidateApply.getApplyStage().size() > 0 ) {
                    int maxNoOfStage = 0 ;
                    String nameOfProcess = "" ;
                    for (ApplyStage applyStage: candidateApply.getApplyStage()) {

                        if(maxNoOfStage < applyStage.getJobStage().getNoOfStage()){
                            maxNoOfStage = applyStage.getJobStage().getNoOfStage();
                            nameOfProcess = applyStage.getJobStage().getRecruitStage().getNameProcess();
                        }
                    }
                    candidateApplyReport.setNameStage(nameOfProcess);


                }
                candidateApplies.add(candidateApplyReport);
            });
        }
        result.setCandidateApplies(candidateApplies);
        // set số candidate applied
        result.setCandidateApplied(candidateApplies.size());

        System.out.println("Time : " + (System.currentTimeMillis() - startTime));
        return result;


    }
    public JobReport convertToJobResponse(Job job) {
        JobReport result  = new JobReport();

        List<SkillListRequest> skillListRequests = new ArrayList<>();
        List<LanguageRequest> languageRequests = new ArrayList<>();
        List<JobStageRequest> jobStageRequests = new ArrayList<>();

        for (SkillLevel skillLevel : job.getSkillLevels()) {
            SkillListRequest skillListRequest = new SkillListRequest();
            skillListRequest.setId(skillLevel.getSkill().getId());
            skillListRequest.setSkillLevelId(skillLevel.getId());
            skillListRequest.setName(skillLevel.getSkill().getSkillName());
            skillListRequest.setPoint(skillLevel.getPoint());
            skillListRequests.add(skillListRequest);
        }

        for (LanguageLevel languageLevel : job.getLanguageLevels()) {
            LanguageRequest languageRequest = new LanguageRequest();
            languageRequest.setLanguageId(languageLevel.getProgramLanguage().getId());
            languageRequest.setLanguageName(languageLevel.getProgramLanguage().getLanguage());
            languageRequest.setPonit(languageLevel.getPonit());
            languageRequest.setLanguageLevelId(languageLevel.getId());
            languageRequests.add(languageRequest);
        }

        for (JobStage jobStage : job.getJobStages()) {
            JobStageRequest jobStageRequest = new JobStageRequest();
            jobStageRequest.setStageId(jobStage.getId());
            jobStageRequest.setNoOf(jobStage.getNoOfStage());
            jobStageRequest.setRecruitStageId(jobStage.getRecruitStage().getId());
            jobStageRequest.setFinishDate(jobStage.getFinishDate());
            jobStageRequest.setCreateDate(jobStage.getCreateDate());
            jobStageRequest.setRewardPercent(jobStage.getRewardPercent());
            jobStageRequest.setPersonQuantity(jobStage.getPersonQuantity());
            jobStageRequest.setRecruitStageName(jobStage.getRecruitStage().getNameProcess());
            jobStageRequest.setStageId(jobStage.getId());
            jobStageRequests.add(jobStageRequest);
            jobStageRequest.setJobStageStatus(jobStage.getJobStageStatus());
            if (jobStage.getJobStageStatus().equals(JobStageStatus.INPROCESS)) {
                result.setAvailableMoneyOfStage((float) jobStage.getAvailableMoney());
            }
            List<TransactionResponse> transactionResponseList = new ArrayList<>();
            for (Transaction transaction: jobStage.getTransactions()) {

                    transactionResponseList.add(convertToTransactionResponse(transaction));
                    if (transaction.getTransactionType() == TransactionType.PAY && transaction.getApplyStage() == null) {
                        jobStageRequest.setTotalPayOfStage(jobStageRequest.getTotalPayOfStage() + transaction.getTotalMoney());
                        result.setTotalMoneyPay(result.getTotalMoneyPay() + transaction.getTotalMoney());
                    }
                    if (transaction.getTransactionType() == TransactionType.RETURN) {
                        jobStageRequest.setTotalPayOfStage(jobStageRequest.getTotalPayOfStage() - transaction.getTotalMoney());
                        result.setTotalMoneyPay(result.getTotalMoneyPay() - transaction.getTotalMoney());
                    }

            }
            transactionResponseList.sort(Comparator.comparing(TransactionResponse::getCreateDate).reversed());
            jobStageRequest.setTransactionResponses(transactionResponseList);

        }
        jobStageRequests.sort(Comparator.comparing(JobStageRequest::getNoOf));
        result.setAssignTo(job.getAssignTo().getId());
        result.setRecruiterStages(jobStageRequests);
        result.setSkillList(skillListRequests);
        result.setLanguageRequests(languageRequests);
        result.setWorkingMode(job.getWorkingMode());
        result.setJobTitle(job.getJobTitle());
        result.setCampus(job.getCampus());
        result.setTitle(job.getTitle());
        result.setSummary(job.getSummary());
        result.setLevel(job.getLevel());
        result.setSalaryForm(job.getSalaryForm());
        result.setSalaryTo(job.getSalaryTo());
        result.setReward(job.getReward());
        result.setImage(job.getImage());
        result.setEmployee_quantity(job.getEmployee_quantity());
        result.setResponsibility(job.getResponsibility());
        result.setJobDescription(job.getJobDescription());
        result.setRequirement(job.getRequirement());
        result.setBenefit(job.getBenefit());
        result.setStatus(job.getStatus());
        result.setId(job.getId());
        result.setCreateBy(job.getCompanyStaff().getId());
        result.setCreateDate(job.getCreateAt());
        result.setCompany(job.getCampus().getCompany());

        return result;
    }

    TransactionResponse convertToTransactionResponse (Transaction transaction){
        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setCode(transaction.getCode());
        transactionResponse.setMoney(transaction.getMoney());
        transactionResponse.setTotalMoney(transaction.getTotalMoney());
        transactionResponse.setPlatformFee(transaction.getPlatformFee());
        transactionResponse.setTransactionType(transaction.getTransactionType());
        transactionResponse.setTransactionResult(transaction.getTransactionResult());
        transactionResponse.setExpireDay(transaction.getExpireDay());
        transactionResponse.setImage_evident(transaction.getImage_evident());
        transactionResponse.setPaymentSide(transaction.getSenderCode());
        transactionResponse.setReceiverCode(transaction.getReceiverCode());
        transactionResponse.setStageName(transaction.getJobStage().getRecruitStage().getNameProcess());
        transactionResponse.setCreateDate(transaction.getCreateDate());
        transactionResponse.setTransferContent(transaction.getTransferContent());
        if(transaction.getApplyStage() != null ){
            transactionResponse.setCandidateApply(transaction.getApplyStage().getCandidateApply().getCvShared().getCv().getFullName());

        }
        return transactionResponse;
    }
}
