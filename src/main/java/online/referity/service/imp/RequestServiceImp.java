package online.referity.service.imp;

import online.referity.dto.request.CreateRequest;
import online.referity.dto.response.HeadhunterRequestResponse;
import online.referity.dto.response.TopHeadhunterResponse;
import online.referity.entity.*;
import online.referity.enums.RequestStatus;
import online.referity.exception.exceptions.BadRequest;
import online.referity.repository.CandidateRepository;
import online.referity.repository.CvSharedRepository;
import online.referity.repository.HeadhunterRepository;
import online.referity.repository.HeadhunterRequestRepository;
import online.referity.service.EmailService;
import online.referity.service.NotificationService;
import online.referity.service.RequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RequestServiceImp implements RequestService {
    @Autowired
    HeadhunterRequestRepository headhunterRequestRepository;

    @Autowired
    CvSharedRepository cvSharedRepository;
    @Autowired
    HeadhunterRepository headhunterRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    NotificationService notificationService ;
    @Autowired
    EmailService emailService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public String createRequest(CreateRequest createRequest, UUID headhunterId) {
        Candidate candidate = null;
        CvShared cvShared = null;
        List<String> to = null;
        String link = "https://referity.online/add-cv/";
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        link = link + headhunter.getId() + "/";
        HeadhunterRequest headhunterRequest = modelMapper.map(createRequest, HeadhunterRequest.class);
        headhunterRequest.setHeadhunter(headhunter);
        headhunterRequest.setCreateAt(LocalDateTime.now());
        headhunterRequest.setDeadlineRequest(LocalDateTime.now().plus(20 , ChronoUnit.DAYS));
        headhunterRequest.setRequestStatus(RequestStatus.WAITING);
        if (createRequest.getCvSharedId() != null) {
            cvShared = cvSharedRepository.getById(createRequest.getCvSharedId());
            candidate = cvShared.getCv().getCandidate();
            headhunterRequest.setCandidate(candidate);
            headhunterRequest.setCvShared(cvShared);
            headhunterRequest.setEmailTo(candidate.getAccount().getEmail());
            headhunterRequest.setId(UUID.randomUUID());
            headhunterRequest = headhunterRequestRepository.save(headhunterRequest);
            to = new ArrayList<>();
            to.add(headhunterRequest.getEmailTo());
            link += headhunterRequest.getId();
            String body = "Headhunter " + headhunter.getAccount().getFullName().toUpperCase() + " was send to your request  " ;
            notificationService.sendNotification("You have request from headhunter "  , body , null , candidate.getAccount().getId());

        }

        if (createRequest.getEmailTo() != null && !createRequest.getEmailTo().isEmpty()) {
            List<HeadhunterRequest> headhunterRequests = new ArrayList<>();
            to = createRequest.getEmailTo();
            for (String email : createRequest.getEmailTo()) {
                candidate = candidateRepository.findCandidateByEmail(email.trim());
                if (candidate != null) {
                    headhunterRequest.setCandidate(candidate);
                    String body = "Headhunter " + headhunter.getAccount().getFullName().toUpperCase() + " was send to your request  " ;
                    notificationService.sendNotification("You have request from headhunter "  , body , null , candidate.getAccount().getId());

                }
                headhunterRequest.setEmailTo(email);
                headhunterRequest.setId(UUID.randomUUID());
                headhunterRequests.add(headhunterRequest);


            }
            link += headhunterRequest.getId();
             headhunterRequestRepository.saveAll(headhunterRequests);
        }
        if (to != null) {
            emailService.sendMailRequest(headhunter.getAccount().getEmail(), "Candidate", to.toArray(new String[0]), link);
            return "Sending request" ;
        }

        return "Please input the address receiver";
    }


    @Override
    public HeadhunterRequest updateRequest(UUID requestId, CreateRequest createRequest) {
        HeadhunterRequest headhunterRequest = headhunterRequestRepository.getById(requestId);
        modelMapper.map(createRequest, headhunterRequest);
        return headhunterRequestRepository.save(headhunterRequest);
    }

    @Override
    public List<HeadhunterRequestResponse> getRequestByCandidateId(UUID candidateId) {
        List<HeadhunterRequest> headhunterRequests = headhunterRequestRepository.getByCandidateId(Sort.by(Sort.Direction.DESC, "createAt"),candidateId);
        List<HeadhunterRequestResponse> headhunterRequestResponses = new ArrayList<>();
        for (HeadhunterRequest headhunterRequest : headhunterRequests) {
            HeadhunterRequestResponse headhunterRequestResponse = modelMapper.map(headhunterRequest, HeadhunterRequestResponse.class);
            TopHeadhunterResponse topHeadhunterResponse = modelMapper.map(headhunterRequest.getHeadhunter(), TopHeadhunterResponse.class);
            topHeadhunterResponse.setFullName(headhunterRequest.getHeadhunter().getAccount().getFullName());
            topHeadhunterResponse.setDescription(headhunterRequest.getHeadhunter().getDescription());
            topHeadhunterResponse.setAvatar(headhunterRequest.getHeadhunter().getAccount().getAvatar());
            headhunterRequestResponse.setHeadhunter(topHeadhunterResponse);
            headhunterRequestResponse.setRequestStatus(headhunterRequest.getRequestStatus());
            headhunterRequestResponses.add(headhunterRequestResponse);
        }
        return headhunterRequestResponses;
    }

    @Override
    public List<HeadhunterRequestResponse> getRequestByHeadhunterId(UUID headhunterId) {
        List<HeadhunterRequest> headhunterRequests = headhunterRequestRepository.getByHeadhunterId(Sort.by(Sort.Direction.DESC , "createAt" ),headhunterId);
        List<HeadhunterRequestResponse> headhunterRequestResponses = new ArrayList<>();
        for (HeadhunterRequest headhunterRequest : headhunterRequests) {
            HeadhunterRequestResponse headhunterRequestResponse = modelMapper.map(headhunterRequest, HeadhunterRequestResponse.class);
            TopHeadhunterResponse topHeadhunterResponse = modelMapper.map(headhunterRequest.getHeadhunter(), TopHeadhunterResponse.class);
            topHeadhunterResponse.setFullName(headhunterRequest.getHeadhunter().getAccount().getFullName());
            topHeadhunterResponse.setDescription(headhunterRequest.getHeadhunter().getDescription());
            topHeadhunterResponse.setAvatar(headhunterRequest.getHeadhunter().getAccount().getAvatar());
            headhunterRequestResponse.setHeadhunter(topHeadhunterResponse);
            headhunterRequestResponses.add(headhunterRequestResponse);
        }
        return headhunterRequestResponses;
    }
}
