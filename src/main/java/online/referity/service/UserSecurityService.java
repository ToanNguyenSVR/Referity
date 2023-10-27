package online.referity.service;

import online.referity.dto.UserSecurityDTO;
import online.referity.entity.Account;
import online.referity.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class UserSecurityService implements UserDetailsService {


    @Autowired
    AccountRepository userRepository;

    public UserDetails loadUserById(UUID id){
        Account user = userRepository.findAccountById(id);
        return new UserSecurityDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Account user = userRepository.findByUsernameOrPhoneOrEmail(id);

        // list role do Spring Security định nghĩa
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        String role;
//        if(user.getInfomation().getWorkUnit() != null){
//            role = user.getInfomation().getWorkUnit().getJob().getName();
//        }else{
//            role = "aaaa";
//        }

//        if(!user.getRoles().isEmpty()){
//            user.getRoles().forEach(roleItem->{
//                authorities.add(new SimpleGrantedAuthority(roleItem.getName()));
//            });
//        }
//
//        if(!user.getInfomation().getJob().getName().isEmpty()){
//            authorities.add(new SimpleGrantedAuthority(user.getInfomation().getJob().getName()));
//        }
//
//        if(authorities.isEmpty()){
//            authorities.add(new SimpleGrantedAuthority("no role"));
//        }

        return new UserSecurityDTO(user);
    }
}
