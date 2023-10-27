package online.referity.exception;

import online.referity.exception.exceptions.ExpiredToken;
import online.referity.exception.exceptions.InValidToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FilterExceptionHandler {

    @ExceptionHandler(InValidToken.class)
    public ResponseEntity<?> invalidToken(InValidToken exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredToken.class)
    public ResponseEntity<?> invalidToken(ExpiredToken exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
