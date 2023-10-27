package online.referity.utils;

import online.referity.exception.exceptions.BadRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Service
  public class Validation {
    public void validate(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for(FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new BadRequest("Invalid request body: " + String.join(", ", errorMessages));
        }
    }
}
