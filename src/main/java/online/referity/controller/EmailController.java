package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.EmailRequest;
import online.referity.dto.request.UpdateLanguageRequest;
import online.referity.entity.ProgramLanguage;
import online.referity.entity.TemplateEmail;
import online.referity.service.EmailService;
import online.referity.utils.Request;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    EmailService service;

    @Autowired
    Request request;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    Validation validation;

    @PostMapping("email-template")
    public ResponseEntity createEmail(@RequestBody EmailRequest emailRequest , BindingResult bindingResult){

        validation.validate(bindingResult);
        TemplateEmail templateEmail = service.createTemplateMail(emailRequest);
        System.out.println(templateEmail);
        return responseHandler.response(200, "Login success!", templateEmail);
    }
    @PutMapping("email-template/{id}")
    public ResponseEntity updateEmail(@RequestBody EmailRequest emailRequest ,@PathVariable int id, BindingResult bindingResult){

        validation.validate(bindingResult);
        TemplateEmail templateEmail = service.update(emailRequest, id);
        System.out.println(templateEmail);
        return responseHandler.response(200, "Login success!", templateEmail);
    }
    @GetMapping("email-template")
    public ResponseEntity getProgramLanguage() {
        List<TemplateEmail> result = service.get();
        return responseHandler.response(200, "Successfully get Teamplate", result);
    }

}
