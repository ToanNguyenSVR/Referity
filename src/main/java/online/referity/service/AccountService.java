package online.referity.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import online.referity.dto.UserSecurityDTO;
import online.referity.dto.request.*;
import online.referity.dto.response.HeadhunterResponse;
import online.referity.dto.response.LoginResponse;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.exception.exceptions.InValidToken;
import online.referity.repository.*;
import online.referity.utils.Helper;
import online.referity.utils.TokenHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class AccountService {



    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CandidateRepository candidateRepository;



    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenHandler tokenHandler;

    @Autowired
    WalletService walletService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyStaffRepository companyStaffRepository;

    @Autowired
    HeadhunterRepository headhunterRepository;

    ModelMapper modelMapper = new ModelMapper();



    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityNotFound("Username or password invalid!");
        }
        Account user = accountRepository.findByUsernameOrPhoneOrEmail(loginRequest.getUsername());
        LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
        loginResponse.setToken(tokenHandler.generateToken((UserSecurityDTO) authentication.getPrincipal()));
        return loginResponse;
    }

    public LoginResponse loginWithToken(LoginRequest loginRequest) {
        UUID id = UUID.fromString(tokenHandler.getInfoByToken(loginRequest.getToken()));
        Account user = accountRepository.findAccountById(id);
        LoginResponse loginResponse = modelMapper.map(user, LoginResponse.class);
        return loginResponse;
    }

    public LoginResponse loginGoogle(GoogleLoginRequest googleLoginRequest){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleLoginRequest.getType() == LoginRequestType.WEB ? "297250691277-ng6be6nmgsi8of0klja20l1b2v5orhnk.apps.googleusercontent.com" : "297250691277-o066mafjoa3g56gpl7i6bifce7rob8fl.apps.googleusercontent.com"))
                .build();

        try{
            GoogleIdToken idToken = verifier.verify(googleLoginRequest.getToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                System.out.println(email);
                Account account = accountRepository.findByUsernameOrPhoneOrEmail(email);
                if(account == null) throw new BadRequest("Account not found!");
                LoginResponse loginResponse = modelMapper.map(account, LoginResponse.class);
                UserSecurityDTO userSecurityDTO = new UserSecurityDTO();
                userSecurityDTO.setUser(account);
                loginResponse.setToken(tokenHandler.generateToken(userSecurityDTO));
                return loginResponse;
            } else {
                throw new InValidToken("Token invalid!!!");
            }
        }catch (GeneralSecurityException e){
            return null;
        }catch (IOException ioException){
            return null;
        }catch (IllegalArgumentException illegalArgumentException){
            // sai định dạng
            throw new InValidToken("Malformed token!!!");
        }
    }

    public Account register(AccountRegisterRequest accountRegisterRequest) {
        Account account = modelMapper.map(accountRegisterRequest, Account.class);
        if(account.getAccountType() == AccountType.COMPANY) account.setAccountType(AccountType.MANAGER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setStatus(AccountStatus.ADDITIONAL_INFORMATION);
        if(account.getAccountType() == AccountType.CANDIDATE) {
            Candidate candidate = new Candidate();
            candidate.setStatus(CandidataStatus.ACTIVE);
            candidate = candidateRepository.save(candidate);
            account.setStatus(AccountStatus.ACTIVE);
            account.setCandidate(candidate);
            candidate.setAccount(account);
        }
        return accountRepository.save(account);
    }




    @Transactional
    public Company additionalInformationForCompany(CompanyAddInformationRequest companyAddInformationRequest, UUID accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account == null) throw new EntityNotFound("Account not found");
        if(account.getAccountType() != AccountType.MANAGER) throw new BadRequest("This account not Allow to setup !");
        if(account.getCompanyStaff() != null) throw new BadRequest("This company already have information!");
        Company company = modelMapper.map(companyAddInformationRequest, Company.class);
        company.setStatus(CompanyStatus.VERIFY);
        company = companyRepository.save(company);
//        String code = Helper.genaralCode("CM" , Math.toIntExact(account.getId()) );
        CompanyStaff companyStaff = new CompanyStaff(null , "MANAGER " , CompanyRole.MANAGER , 0,false , company , account , null,null );
        companyStaff = companyStaffRepository.save(companyStaff);
        account.setStatus(AccountStatus.VERIFY);
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setBeneficiaryAccount(companyAddInformationRequest.getBeneficiaryAccount());
        walletRequest.setBeneficiaryBank(companyAddInformationRequest.getBeneficiaryBank());
        walletRequest.setBeneficiaryName(companyAddInformationRequest.getBeneficiaryName());
        walletService.initWallet(walletRequest , account);
        return company;
    }

    public Account additionalInformationForHeadhunter(HeadhunterAdditionalInformation headhunterAdditionalInformation, UUID accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account == null) throw new EntityNotFound("Account not found");
        if(account.getAccountType() != AccountType.HEADHUNTER) throw new BadRequest("This account not headhunter!");
        if(account.getHeadhunter() != null) throw new BadRequest("This headhunter already have information!");
        Headhunter headhunter = modelMapper.map(headhunterAdditionalInformation, Headhunter.class);
//        headhunter.setUuid(UUID.randomUUID());
        headhunter.setAccount(account);
        account.setStatus(AccountStatus.VERIFY);
        account.setHeadhunter(headhunter);
        headhunterRepository.save(headhunter);
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setBeneficiaryAccount(headhunterAdditionalInformation.getBeneficiaryAccount());
        walletRequest.setBeneficiaryBank(headhunterAdditionalInformation.getBeneficiaryBank());
        walletRequest.setBeneficiaryName(headhunterAdditionalInformation.getBeneficiaryName());
        walletService.initWallet(walletRequest , account);
        return account;
    }

    public Account updateInformationForHeadhunter(HeadhunterAdditionalInformation headhunterAdditionalInformation, UUID accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account == null) throw new EntityNotFound("Account not found");
        if(account.getAccountType() != AccountType.HEADHUNTER) throw new BadRequest("This account not headhunter!");
        if(account.getHeadhunter() == null) throw new BadRequest("This headhunter don't have information!");
        Headhunter headhunter = modelMapper.map(headhunterAdditionalInformation, Headhunter.class);
        headhunter.setAccount(account);
        account.setStatus(AccountStatus.VERIFY);
        account.setHeadhunter(headhunter);
        headhunter.setId(account.getHeadhunter().getId());
        headhunterRepository.save(headhunter);
        return account;
    }

    public Company updateInformationForCompany(CompanyAddInformationRequest companyAddInformationRequest, UUID accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account == null) throw new EntityNotFound("Account not found");
        if(account.getAccountType() != AccountType.COMPANY) throw new BadRequest("This account not company!");
      //  if(account.getCompany() == null) throw new BadRequest("This company don't have information!");
        Company company = modelMapper.map(companyAddInformationRequest, Company.class);
        account.setStatus(AccountStatus.ACTIVE);
       // company.setAccount(account);
        account.setStatus(AccountStatus.VERIFY);
       // account.setCompany(company);
        //company.setId(account.getCompany().getId());
        companyRepository.save(company);
        return company;
    }

    public Account getAccountDetail(UUID accountId){
        Optional<Account> account = accountRepository.findById(accountId);
        if(!account.isPresent() ) throw new BadRequest("Account not found!");
        return account.get();
    }

    public Account verifyAccount(UUID accountId){
        Account account = getAccountDetail(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    public Account blockAccount(UUID accountId){
        Account account = getAccountDetail(accountId);
        account.setStatus(AccountStatus.SUSPENDED);
        return accountRepository.save(account);
    }

    public List<HeadhunterResponse> getHeadhunter(AccountStatus accountStatus){
        List<HeadhunterResponse> headhunterResponses = new ArrayList<>();
        List<Headhunter> headhunters = headhunterRepository.getHeadhunter(accountStatus);
        for (Headhunter headhunter: headhunters) {

            headhunterResponses.add(convertHeadhunter(headhunter));
        }
        return headhunterResponses;

    }
    public  HeadhunterResponse  convertHeadhunter ( Headhunter headhunter ){
        HeadhunterResponse headhunterResponse = modelMapper.map(headhunter , HeadhunterResponse.class);
        headhunterResponse.setAvatar(headhunter.getAccount().getAvatar());
        headhunterResponse.setEmail(headhunter.getAccount().getEmail());
        headhunterResponse.setFullName(headhunter.getAccount().getFullName());
        headhunterResponse.setStatus(headhunter.getAccount().getStatus());
        headhunterResponse.setPhone(headhunter.getAccount().getPhone());
        headhunterResponse.setCode(headhunter.getCode());
        headhunterResponse.setAccountId(headhunter.getAccount().getId());
        return headhunterResponse;
    }



}
