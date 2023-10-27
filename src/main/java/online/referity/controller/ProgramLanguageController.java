package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.UpdateLanguageRequest;
import online.referity.dto.response.LanguageResponse;
import online.referity.entity.*;
import online.referity.service.ProgramLanguageService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class ProgramLanguageController {
    @Autowired
    ProgramLanguageService service;

    @Autowired
    ResponseHandler responseHandler;


    @GetMapping("language/{key}")
    @Operation(summary = "Seach Program language  ")
    public ResponseEntity seachProgram(@PathVariable String key) {
        List<ProgramLanguage> result = service.seachLanguage(key);
        return ResponseEntity.ok(result);
    }

    @GetMapping("language")
    public ResponseEntity getProgramLanguage() {
        List<ProgramLanguage> result = service.getProgramLanguage();
        return responseHandler.response(200, "Successfully get program language", result);
    }

    @PostMapping("languages")
    @Operation(summary = " Create list language  ")
    public ResponseEntity creates (@Valid @RequestBody List<ProgramLanguage> key ){
        List<ProgramLanguage> result = service.creates(key);
        ResponseEntity response = responseHandler.response(201, "Successfully update program language !", result);
        return response;
    }
    @PostMapping("language")
    @Operation(summary = "Create language  ")
    public  ResponseEntity create (@Valid @RequestBody ProgramLanguage languageRequest ){
        ProgramLanguage result = service.create(languageRequest.getLanguage());
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @PutMapping("language/{languageId}")
    @Operation(summary = "Update language of system ")
    public ResponseEntity updateStage(@Valid @RequestBody UpdateLanguageRequest language, @PathVariable UUID languageId) {
        ProgramLanguage result = service.update(language, languageId);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @DeleteMapping("language/{languageId}")
    @Operation(summary = "Delete language of system ")
    public ResponseEntity updateStage(@PathVariable UUID languageId) {
        ProgramLanguage result = service.delete(languageId);
        ResponseEntity response = responseHandler.response(201, "Successfully deleted stage of company!", result);
        return response;
    }


}
