package online.referity.service.imp;

import online.referity.dto.request.EmailRequest;
import online.referity.entity.TemplateEmail;
import online.referity.enums.Emailtype;
import online.referity.exception.exceptions.BadRequest;
import online.referity.repository.EmailRepository;
import online.referity.service.EmailService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private  String sender ;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String sendSimpeeMail(String[] to , String cc , String content , String subject) {

        try {
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setCc(cc);
            javaMailSender.send(mimeMessage);

        }catch (Exception e){

            logger.error("An error occurred: {}", e.getMessage(), e);
        }
        return "Mail Send sucessfully";
    }

    @Override
    public TemplateEmail createTemplateMail(EmailRequest email) {
        TemplateEmail tempMail = modelMapper.map(email, TemplateEmail.class);
        return emailRepository.save(tempMail);
    }

    @Override
    public void sendMailRequest(String cc, String dear, String[] toEmail, String link) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    TemplateEmail email = emailRepository.getByType(Emailtype.NOTI_CANDIDATE);
                    Context context = new Context();
                    context.setVariable("name", dear);
                    context.setVariable("content" ,  email.getContent());
                    context.setVariable("by" , email.getFooter());
                    context.setVariable("link" , link);
                    String text = templateEngine.process("emailtemplate", context);
                    sendSimpeeMail(toEmail , cc , text , email.getSubject()  );

                }catch (Exception e){
                    logger.error("An error occurred: {}", e.getMessage(), e);


                }
                logger.info("Send mail verify sucessful to " + toEmail);

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }


    @Override
    public void sendMailVerify(String cc , String dear , String[] toEmail , String link ) {
        try{
            TemplateEmail email = emailRepository.getByType(Emailtype.VERIFY_CANDIDATE);
            Context context = new Context();
            context.setVariable("name", dear);
            context.setVariable("content" ,  email.getContent() +link );
            context.setVariable("by" , email.getFooter());
            String text = templateEngine.process("emailtemplate", context);
            sendSimpeeMail(toEmail , cc , text , email.getSubject()  );

    }catch (Exception e){

        logger.error("An error occurred: {}", e.getMessage(), e);
    }
        logger.info("Send mail verify sucessful to " + toEmail);
    }

    @Override
    public TemplateEmail update(EmailRequest emailRequest, int id) {

        Optional<TemplateEmail> email = emailRepository.findById(id);
        if(!email.isPresent()) throw  new BadRequest("Email template not exist ");

        modelMapper.map(emailRequest , email.get());
        return emailRepository.save(email.get());
    }

    @Override
    public TemplateEmail delete(int id) {
        Optional<TemplateEmail> email = emailRepository.findById(id);
        if(!email.isPresent()) throw  new BadRequest("Email template not exist ");
        email.get().setDelete(true);
        return emailRepository.save(email.get());
    }

    @Override
    public List<TemplateEmail> get() {
        return emailRepository.findAll(Sort.by("id"));
    }
}
