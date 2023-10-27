package online.referity.utils;

import online.referity.dto.UserSecurityDTO;
import online.referity.entity.Account;
import online.referity.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Request {

    @Autowired
    AccountRepository userRepository;
    
    public Account getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDTO user = (UserSecurityDTO) authentication.getPrincipal();
        return userRepository.findByUsernameOrPhoneOrEmail(user.getUser().getPhone());
    }
    
}
