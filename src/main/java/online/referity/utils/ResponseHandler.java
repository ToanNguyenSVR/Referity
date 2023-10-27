package online.referity.utils;

import online.referity.dto.ResponseDTO;
import online.referity.dto.ResponseWithDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandler<T> {

    public ResponseEntity response(int code, String message, T data){
        if(data == null)
            return ResponseEntity.ok(new ResponseDTO(code, message));
        return ResponseEntity.ok(new ResponseWithDataDTO<T>(code, message, data));
    }

}
