package online.referity.service.imp;

import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.UpdateJobStageRequest;
import online.referity.dto.response.ConfirmBillStageResponse;
import online.referity.dto.response.NextStageResponse;
import online.referity.dto.response.PresentStageResponse;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.Duplicate;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.*;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobConfigServiceImp implements JobConfigService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    ApplyStageRepository applyStageRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    JobStageRepository jobStageRepository;
    @Autowired
    RecruitStageRepository recruitStageRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CandidateService candidateService;
    @Autowired
    JobRepository jobRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Override
    public JobStage createJobStage(JobStageRequest jobStageRequest, UUID jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (!job.isPresent()) throw new EntityNotFound("Job not found with id : " + jobId);
        Optional<RecruitStage> recruitStage = recruitStageRepository.findById(jobStageRequest.getRecruitStageId());
        if (!recruitStage.isPresent())
            throw new EntityNotFound("Recruit stage not found with id : " + jobStageRequest.getRecruitStageId());
        JobStage jobStage = jobStageRepository.findByJobIdAndRecruitStageId(jobId, jobStageRequest.getRecruitStageId());
        if (jobStage != null)
            throw new Duplicate("Job Stage with Recruit Stage  : '" + recruitStage.get().getNameProcess() + "' was exist please update information of this job stage : " + recruitStage.get().getId());
        jobStage = modelMapper.map(jobStageRequest, JobStage.class);
        if (jobStage.getRewardPercent() < recruitStage.get().getBaseStageRewardPresent())
            throw new BadRequest("Reward percent of job stage less than recruit stage ");
        double rewardForOne = ((float) jobStage.getRewardPercent() / 100) * job.get().getReward();
        double rewardForAll = (((float) jobStage.getRewardPercent() / 100) * job.get().getReward()) * jobStage.getPersonQuantity();
        jobStage.setId(null);
        jobStage.setJob(job.get());
        jobStage.setNoOfStage(recruitStage.get().getNoOfStage());
        jobStage.setRewardPredictedForOne(rewardForOne);
        jobStage.setRewardPredictedForALL(rewardForAll);
        jobStage.setRecruitStage(recruitStage.get());
        jobStage.setJobStageStatus(JobStageStatus.NEW);
        return jobStageRepository.save(jobStage);
    }

    @Override
    public JobStage updateJobStage(JobStageRequest jobStageRequest) {
        Optional<JobStage> jobStageOptional = jobStageRepository.findById(jobStageRequest.getStageId());
        if (!jobStageOptional.isPresent())
            throw new EntityNotFound("Job stage not found with id : " + jobStageRequest.getStageId());
        JobStage jobStage = jobStageOptional.get();
        jobStage.setPersonQuantity(jobStageRequest.getPersonQuantity());
        jobStage.setRewardPercent(jobStageRequest.getRewardPercent());
        jobStage.setFinishDate(jobStageRequest.getFinishDate());
        double rewardForOne = ((float) jobStage.getRewardPercent() / 100) * jobStage.getJob().getReward();
        double rewardForAll = (((float) jobStage.getRewardPercent() / 100) * jobStage.getJob().getReward()) * jobStage.getPersonQuantity();
        jobStage.setRewardPredictedForOne(rewardForOne);
        jobStage.setRewardPredictedForALL(rewardForAll);

        return jobStageRepository.save(jobStage);
    }


//    public Transaction updateStatus(UUID jobStageId, UUID jobId ,SetUpStage status) {
//        Job job = jobRepository.findJobById(jobId);
//        JobStage jobStage = null ;
//        if(jobStageId == null && status == SetUpStage.OPEN){
//            jobStage = jobStageRepository.findByJobIdAndNoOfStage(job.getId() , 1);
//        }else {
//            Optional<JobStage> jobStageOptional = jobStageRepository.findById(jobStageId);
//            if(!jobStageOptional.isPresent()) throw  new EntityNotFound("JobStage Not Found ");
//            jobStage = jobStageOptional.get();
//        }
//        Account account = accountRepository.getManagerByCompaniId(jobStage.getJob().getCompanyStaff().getCompany().getId());
//        if(jobStage.getJob().getId() != job.getId()) throw new BadRequest("404 Not Found");
//        String key = "TPO" + jobStage.getId() +"0"+ job.getId() ;
//        switch (status){
//            case OPEN:
//                if(jobStage.getJobStageStatus() != JobStageStatus.NEW) throw  new BadRequest("Job Stage can't  start ");
//                String content  = "Prepayment for Job : " + jobStage.getJob().getJobTitle() + " Id : " + jobStage.getJob().getId() + " with Stage :" + jobStage.getRecruitStage().getNameProcess();
//                key = key+ "0";
//                Transaction transaction = transactionService.createTransaction(
//                        null ,
//                        jobStage
//                        , key
//                        , account.getWallet()
//                        ,jobStage.getRewardPredictedForALL()
//                        ,TransactionType.PAY
//                        , TransactionResult.ACTIVE
//                        , content
//                        , account.getCompanyStaff().getCode()
//                        , "TO BOCKED MONEY OF STAGE");
//                if(jobStage.getTransactions().isEmpty()) jobStage.setTransactions(new ArrayList<>());
//                jobStage.getTransactions().add(transaction);
//                break;
//            case CLOSE:
//                jobStage = closeStage(jobStage , key , account);
//                break;
//            default:
//                throw new BadRequest("ERROR STAGE STATUS");
//        }
//        return null ;
//    }

    @Override
    @Transactional
    public ApplyStage acceptApply(UUID applyId, ResultApply resultApply, UUID accountCompanyId, String note) {
        ApplyStage applyStage = applyStageRepository.getApplyStageById(applyId);
        if (applyStage == null)
            throw new BadRequest("Server error");
        if (applyStage.getStatus() != ResultStage.IN_PROCESS) throw new BadRequest(" Apply was processed ");
        JobStage jobStage = applyStage.getJobStage();
        Headhunter headhunter = applyStage.getCandidateApply().getCvShared().getHeadhunter();
        Account account = accountRepository.findAccountById(accountCompanyId);
        Account accountManager = jobStage.getJob().getCompanyStaff().getAccount() ;
        if ( !account.getId().equals(jobStage.getJob().getAssignTo().getAccount().getId()) && !account.getAccountType().equals(AccountType.MANAGER))
            throw new BadRequest("You do not have permission to perform this operation");
        if (jobStage.getAvailableMoney() < jobStage.getRewardPredictedForOne())
            throw new BadRequest("Your job stage has run out of money");
        String messNoti;
        switch (resultApply) {
            case ACCEPT:
                String key = "TP0" + jobStage.getNoOfStage() + "0" + applyStage.getId().toString().substring(0, 4);
                String content = "Payment for " + applyStage.getCandidateApply().getCvShared().getCv().getFullName() + "( of Headhunter : " + applyStage.getCandidateApply().getCvShared().getHeadhunter().getAccount().getFullName() + ")" + "By round " + jobStage.getNoOfStage() + " " + " ( " + jobStage.getRecruitStage().getNameProcess() + " ) ";
                double availableMoney = jobStage.getAvailableMoney() - jobStage.getRewardPredictedForOne();
                applyStage.setStatus(ResultStage.PASS);
                applyStage.setNote("Passed round " + jobStage.getRecruitStage().getNoOfStage());
                int percentProcess = applyStage.getCandidateApply().getPersentProgess() + 100 / (jobStage.getJob().getJobStages().size());
                applyStage.getCandidateApply().setPersentProgess(percentProcess);
                // Tạo transaction ghi nhận hành động cộng tiền

                Transaction transaction = transactionService.createTransaction(
                        applyStage,
                        jobStage,
                        key,
                        headhunter.getAccount().getWallet(),
                        jobStage.getRewardPredictedForOne(),
                        TransactionType.PAY,
                        TransactionResult.PAYED,
                        content,
                        jobStage.getJob().getCompanyStaff().getId().toString().substring(0, 4),
                        headhunter.getCode());
                // trừ tiền
                walletService.updateMoney(headhunter.getAccount().getWallet().getId(), jobStage.getRewardPredictedForOne(), TransactionType.PAY);
                accountManager.getWallet().setBlockMoney(accountManager.getWallet().getBlockMoney() - jobStage.getRewardPredictedForOne());
                walletService.updateMoneyAdmin(transaction.getTransactionType(), AccountType.ADMIN, AccountType.HEADHUNTER, "PAY FOR APPLY STAGE :" + applyStage.getCode(), transaction.getTotalMoney());
                jobStage.setAvailableMoney(availableMoney);
                messNoti = "Congratulation You was have " + jobStage.getRewardPredictedForOne() + "$ in this stage.";
                /// cos mail thongo baos ve cho headhunter vad candidate
                break;
            case REJECT:
                applyStage.setStatus(ResultStage.FAIL);
                applyStage.getCandidateApply().setResultApply(ResultStage.FAIL);
                applyStage.setNote(note);
                messNoti = "Recruiter notice is " + note;
                break;
            default:
                throw new BadRequest("Please select result ");

        }
        if (applyStage.getCandidateApply().getPersentProgess() == 80)
            candidateService.updateStatusDone(applyStage.getCandidateApply().getCvShared().getCv().getCandidate().getId());
        String body = "Candidate " + applyStage.getCandidateApply().getCvShared().getCv().getFullName().toUpperCase() + " was " + applyStage.getStatus()
                + " in round " + applyStage.getJobStage().getNoOfStage() + " of " + applyStage.getJobStage().getRecruitStage().getNameProcess() + "." + messNoti;
        notificationService.sendNotification("Company was confirm your apply ", body, null, headhunter.getAccount().getId());
        applyStage.getCandidateApply().setNote(applyStage.getNote());
        applyStage.getCandidateApply().setUpdateAt(LocalDateTime.now());
        accountRepository.save(accountManager);
        jobStageRepository.save(jobStage);
        return applyStageRepository.save(applyStage);

    }

    public ConfirmBillStageResponse getBillOfStage(UUID jobStageId) {
        Optional<JobStage> jobStageOptional = jobStageRepository.findById(jobStageId);
        if (!jobStageOptional.isPresent()) throw new EntityNotFound("JobStage Not Found ");
        ConfirmBillStageResponse bill = new ConfirmBillStageResponse();
        double returnMoney = 0.0;
        int cvPassedLastStage = 0;
        boolean isClose = false;
        boolean isOpen = false;
        PresentStageResponse presentStageResponse = new PresentStageResponse();
        NextStageResponse nextStageResponse = new NextStageResponse();
        JobStage nextStage = new JobStage() ;
        JobStage jobStage = jobStageOptional.get();
       int platformFee = systemConfigRepository.findSystemConfigByName(SystemConfigType.FEE_SYSTEM).getValue();

        nextStage = jobStageRepository.findByJobIdAndNoOfStage(jobStage.getJob().getId(), jobStage.getNoOfStage() + 1);

        if (jobStage.getJobStageStatus() == JobStageStatus.NEW) {
            isOpen = true;
        }
        if (jobStage.getJobStageStatus() == JobStageStatus.CLOSE) throw new BadRequest("Job stage was close");

            if (nextStage == null) {
                isClose = true;
            }


            presentStageResponse.setCvAppliedInStage(jobStage.getApplyStages().size());
            for (ApplyStage applyStage : jobStage.getApplyStages()) {
                switch (applyStage.getStatus()) {
                    case FAIL:
                        presentStageResponse.setCvFailInStage(presentStageResponse.getCvAppliedInStage() + 1);
                        break;
                    case PASS:
                        presentStageResponse.setCvPassedInStage(presentStageResponse.getCvPassedInStage() + 1);
                        break;
                    case IN_PROCESS:
                        presentStageResponse.setCvInProcessInStage(presentStageResponse.getCvInProcessInStage() + 1);
                        break;

                }
            }
            presentStageResponse.setStageName(jobStage.getRecruitStage().getNameProcess());
            presentStageResponse.setStageId(jobStage.getId());
            presentStageResponse.setPersonTargetInStage(jobStage.getPersonQuantity());
            presentStageResponse.setReturnMoney(jobStage.getAvailableMoney());
            presentStageResponse.setRewardPercent(jobStage.getRewardPercent());
            presentStageResponse.setBlockMoney(jobStage.getRewardPredictedForALL());
            presentStageResponse.setPaidMoney(jobStage.getRewardPredictedForOne() * presentStageResponse.getCvPassedInStage());
            presentStageResponse.setPlatformFee(platformFee);
            presentStageResponse.setRewardPredictedForOne(jobStage.getRewardPredictedForOne());


        if(!isClose){
            nextStageResponse.setStageName(nextStage.getRecruitStage().getNameProcess());
            nextStageResponse.setStageId(nextStage.getId());
            nextStageResponse.setRewardPredictedForALL(nextStage.getRewardPredictedForALL());
            nextStageResponse.setPlatformFee(platformFee);
            nextStageResponse.setNoOfStage(nextStage.getNoOfStage());
            nextStageResponse.setPersonQuantity(nextStage.getPersonQuantity());
            nextStageResponse.setRewardPercent(nextStage.getRewardPercent());
            nextStageResponse.setRewardPredictedForOne(nextStage.getRewardPredictedForOne());
            nextStageResponse.setTotalMoney(nextStage.getRewardPredictedForALL()+nextStage.getRewardPredictedForALL()*platformFee/100 );
        }

        bill.setPresentStageResponse(presentStageResponse);
        bill.setNextStageResponse(nextStageResponse);
        bill.setClose(isClose);
        return bill;
    }

    @Transactional
    public JobStage startStage(UUID jobStageId, UUID accountId) {
        Optional<JobStage> jobStageOptional = jobStageRepository.findById(jobStageId);
//        if(!jobStageOptional.isPresent()) throw  new EntityNotFound("JobStage Not Found ");
        JobStage jobStage = jobStageOptional.get();
        final JobStage js = jobStage;
        if (jobStage.getJob().getStatus() == JobStatus.PENDING) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("matching start");
                    List<MatchingScore> matchingScores = matchingRepository.findMatchingScoreByJobId(js.getJob().getId());
                    for (MatchingScore matchingScore : matchingScores) {
                        if (matchingScore.getScore() > 50) {
                            for (CvShared cvShared : matchingScore.getCv().getCvSharedList()) {
                                System.out.println("matching: " + matchingScore.getId());
                                notificationService.sendNotification("Matching detection", "Your candidate " + matchingScore.getCv().getFullName() + " matching with " + matchingScore.getJob().getJobTitle().getPosition(), "", cvShared.getHeadhunter().getAccount().getId());
                            }
                        }
                    }
                }
            };
            new Thread(r).start();
        }

        Account account = accountRepository.findAccountById(accountId);
        List<ApplyStage> newApplyStage = new ArrayList<>();
        if (account == null
                || account.getCompanyStaff().getCompany() != jobStage.getJob().getCompanyStaff().getCompany())
            throw new BadRequest("Your account don't have permission to do . Please contact to MANAGER");
        Account manager = accountRepository.getManagerByCompaniId(account.getCompanyStaff().getCompany().getId());
        if (jobStage.getJobStageStatus() == JobStageStatus.CLOSE) throw new BadRequest("Job stage was close");
        if (jobStage.getJobStageStatus() == JobStageStatus.INPROCESS) {
            Transaction transaction = deductionMoneyTransaction(jobStage, manager);
            transaction.setTransactionResult(TransactionResult.PAYED);
            jobStage.setJobStageStatus(JobStageStatus.CLOSE);
            List<ApplyStage> applyStages = applyStageRepository.getApplyByJobStage(jobStage.getId(), ResultStage.PASS);
            List<ApplyStage> rejectApply = applyStageRepository.getApplyDifferenceResultStage(jobStage.getId(), ResultStage.PASS);
            if(jobStage.getJob().getStatus() == JobStatus.ACTIVE && applyStages.isEmpty() && rejectApply.isEmpty() ){
                throw  new BadRequest("The job has no applications. Please check and edit the job to make it more attractive to candidates.");
            }
            for (ApplyStage ap : rejectApply) {
                ap.setStatus(ResultStage.FAIL);
                ap.getCandidateApply().setResultApply(ResultStage.FAIL);
            }
            applyStageRepository.saveAll(rejectApply);
            jobStageRepository.save(jobStage);
            jobStage = jobStageRepository.findByJobIdAndNoOfStage(jobStage.getJob().getId(), jobStage.getNoOfStage() + 1);

            if (jobStage != null) {
                for (ApplyStage applyS : applyStages) {
                    String code = "AS" + applyS.getCandidateApply().getId().toString().substring(0, 3) + "0" + jobStage.getId().toString().substring(0, 3);
                    ApplyStage applyStage = new ApplyStage(
                            null
                            , code
                            , "WAS PASS ROUND : " + jobStage.getRecruitStage().getNameProcess()
                            , ResultStage.IN_PROCESS
                            , jobStage
                            , null
                            , applyS.getCandidateApply());
                    newApplyStage.add(applyStage);
                    // đéo hiểu thêm lol này chi
                    //jobStageOptional.get().getJob().setStatus(JobStatus.DONE);
                }
                if (jobStage.getJob().getStatus() == JobStatus.ACTIVE) {
                    jobStage.getJob().setStatus(JobStatus.INPROCESS);
                }
                applyStageRepository.saveAll(newApplyStage);
            } else {
                for (ApplyStage applyStage: applyStages) {
                    applyStage.getCandidateApply().setResultApply(ResultStage.PASS);
                }
                jobStageOptional.get().getJob().setStatus(JobStatus.DONE);
                jobStageRepository.save(jobStageOptional.get());
                applyStageRepository.saveAll(applyStages);
                return jobStageOptional.get();

            }

        }
        // updste lai tien cua admin
        Transaction transaction = blockMoneyTransaction(jobStage, manager);
        transaction.setTransactionResult(TransactionResult.PAYED);
        jobStage.setAvailableMoney(transaction.getMoney());
        jobStage.setJobStageStatus(JobStageStatus.INPROCESS);
        if (jobStage.getNoOfStage() == 1) jobStage.getJob().setStatus(JobStatus.ACTIVE);
        jobStage = jobStageRepository.save(jobStage);
        return jobStage;
    }


//
//    @Override
//    public Transaction getTransaction(UUID jobStageId, UUID jobId, SetUpStage status) {
//        Job job = jobRepository.findJobById(jobId);
//        JobStage jobStage = null ;
//        Transaction transaction = transactionRepository.findByJobStageId(jobStage.getId() , TransactionType.PAY);
//        if(transaction != null ) return transaction ;
//        Account account = accountRepository.getManagerByCompaniId(jobStage.getJob().getCompanyStaff().getCompany().getId());
//        if(jobStage.getJob().getId() != job.getId()) throw new BadRequest("404 Not Found");
//        String key = "TPO" + jobStage.getId() +"0"+ job.getId() ;
//        switch (status){
//            case OPEN:
//                jobStage = jobStageRepository.findByJobIdAndNoOfStage(job.getId() , 1);
//
//                break;
//            case CLOSE:
//                Optional<JobStage> jobStageOptional = jobStageRepository.findById(jobStageId);
//                if(!jobStageOptional.isPresent()) throw  new EntityNotFound("JobStage Not Found ");
//                jobStage = jobStageOptional.get();
//                blockMoneyTransaction(jobStage , account);
//                break;
//            default:
//                throw new BadRequest("ERROR STAGE STATUS");
//        }
//        return transaction ;
//    }

    public Transaction blockMoneyTransaction(JobStage jobStage, Account account) {

        if (jobStage.getJobStageStatus() == JobStageStatus.CLOSE) throw new BadRequest("STAGE WAS CLOSED !!!");
        String contentNextStage = "Prepayment for Job : " + jobStage.getJob().getTitle() + " with Stage :" + jobStage.getRecruitStage().getNameProcess();
        String key = "TPO" + jobStage.getId() + "0" + jobStage.getJob().getId();
        Transaction transaction = transactionService.createTransaction(
                null
                , jobStage
                , key
                , account.getWallet()
                , jobStage.getRewardPredictedForALL()
                , TransactionType.PAY
                , TransactionResult.ACTIVE
                , contentNextStage
                , account.getCompanyStaff().getId().toString()
                , "TO BOCKED MONEY OF STAGE");
        jobStage.getTransactions().add(transaction);
        walletService.updateMoney(account.getWallet().getId(), transaction.getTotalMoney(), transaction.getTransactionType());
        walletService.updateMoneyAdmin(transaction.getTransactionType(), AccountType.COMPANY, AccountType.ADMIN, "Company " + contentNextStage, transaction.getTotalMoney());
        return transaction;


    }

    public Transaction deductionMoneyTransaction(JobStage jobStage, Account account) {
        if (jobStage.getJobStageStatus() == JobStageStatus.CLOSE) throw new BadRequest("STAGE WAS CLOSED !!!");
        String contentNextStage = "Prepayment for Job : " + jobStage.getJob().getTitle() +  " with Stage :" + jobStage.getRecruitStage().getNameProcess();
        String key = "TPO" + jobStage.getId() + "0" + jobStage.getJob().getId();
        Transaction transaction = transactionService.createTransaction(
                null
                , jobStage
                , key
                , account.getWallet()
                , jobStage.getAvailableMoney()
                , TransactionType.RETURN
                , TransactionResult.PAYED
                , contentNextStage
                , account.getCompanyStaff().getId().toString()
                , "TO RETURN MONEY OF STAGE " + jobStage.getRecruitStage().getNameProcess());
        jobStage.getTransactions().add(transaction);
        jobStage.setAvailableMoney(0);

        walletService.updateMoney(account.getWallet().getId(), jobStage.getAvailableMoney(), transaction.getTransactionType());
        walletService.updateMoneyAdmin(transaction.getTransactionType(), AccountType.ADMIN, AccountType.COMPANY, "RETURN MONEY OF STAGE :" + jobStage.getId(), transaction.getTotalMoney());
        return transaction;
    }


//    String content = "DEDUCTION TO COMPANY";
//    applyStages= applyStageRepository.getApplyByJobStage(jobStage.getId() , ResultStage.PASS);
//    key = key + applyStages.size();
//    double rewardOfStage = applyStages.size() * jobStage.getRewardPredictedForOne();
//        transactionService.createTransaction(
//                null
//                ,jobStage
//                , key
//                , account.getWallet()
//                        // null vì sau khi vào trong create
//                        , jobStage.getRewardPredictedForALL()
//                        , TransactionType.DEDUCTION
//                , TransactionResult.PAYED
//                , content
//                , "SYSTEM AUTOMATIC"
//                        , account.getCompanyStaff().getCode());
//
//        if(jobStage.getAvailableMoney() > 0 ){
//    walletService.updateMoney(account.getWallet().getId()
//            , jobStage.getAvailableMoney()
//            , TransactionType.DEDUCTION);
//}
//    for (ApplyStage applyS: applyStages) {
//    String code = "AS" + applyS.getCandidateApply().getId() + "0" + jobStage.getId() ;
//    ApplyStage applyStage = new ApplyStage(
//            null
//            , code
//            ,"WAS PASS ROUND : " + jobStage.getRecruitStage().getNameProcess()
//            , ResultStage.IN_PROCESS
//            ,nextStage
//            ,null
//            , applyS.getCandidateApply());
//    newApplyStage.add(applyStage);
//}


}
