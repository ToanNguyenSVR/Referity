package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.RatingRequest;
import online.referity.dto.response.RatingResponse;
import online.referity.service.RatingService;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @Autowired
    ResponseHandler responseHandler;
    @Autowired
    Validation validation;

    @PostMapping("rating")
    public ResponseEntity create (@Valid @RequestBody RatingRequest request , BindingResult vaild){
        validation.validate(vaild);
        RatingResponse result = ratingService.createRating(request);
        return responseHandler.response(200, "Successfully rating headhunter", result);
    }

    @GetMapping("rating")
    @Operation(description = "Get ratings ")
    public ResponseEntity getRating (@RequestParam(required = false) Integer start ,
                                  @RequestParam(required = false) UUID headhunterId ,
                                  @RequestParam(required = false) UUID companyId
                                  ){

        List<RatingResponse> result = ratingService.getRating(start, headhunterId, companyId);
        return responseHandler.response(200, "Successfully get rating ", result);
    }

}
