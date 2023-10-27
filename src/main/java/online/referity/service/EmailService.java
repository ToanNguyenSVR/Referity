package online.referity.service;

import online.referity.dto.request.EmailRequest;
import online.referity.entity.TemplateEmail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {

     public String sendSimpeeMail(String[] to , String cc , String content , String subject);

     TemplateEmail createTemplateMail (EmailRequest email);
     void sendMailRequest (String cc , String dear , String[] toEmail , String link );

     public void sendMailVerify(String cc , String dear , String[] toEmail , String link ) ;

     TemplateEmail update (EmailRequest emailRequest , int id);
     TemplateEmail delete ( int id);

     List<TemplateEmail> get();

}
