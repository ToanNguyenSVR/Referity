package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.entity.SkillLevel;
import online.referity.entity.TemplateEmail;
import online.referity.entity.Skill;
import online.referity.entity.SkillGroup;
import online.referity.service.EmailService;
import online.referity.service.SkillService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController

@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class SkillController {

    @Autowired
    SkillService skillService;
    @Autowired
    EmailService emailService;

    @Autowired
    ResponseHandler responseHandler;

    @GetMapping("skill/{key}")
    @Operation(summary = "Seach skill ")
    public ResponseEntity seachSkill(@PathVariable String key) {
        List<Skill> result = skillService.seachSkill(key);
        return ResponseEntity.ok(result);
    }

    @GetMapping("skill")
    @Operation(summary = "Get skill ")
    public ResponseEntity getSkill() {
        List<Skill> result = skillService.getSkill();
        return responseHandler.response(200, "Successfully get skill list", result);
    }

    @GetMapping("skill-group")
    @Operation(summary = "Seach skill group ")
    public ResponseEntity seachGroup() {
        List<SkillGroup> result = skillService.seachGroup();
        return responseHandler.response(200, "Successfully get skill group", result);
    }

    @PutMapping("skill-group/{id}")
    @Operation(summary = "update skill group ")
    public ResponseEntity updatGroup(@RequestBody SkillGroup data, @PathVariable UUID id) {
        SkillGroup result = skillService.updateGroup(data, id);
        return responseHandler.response(200, "Successfully update skill group", result);
    }

    @PutMapping("skill/{id}")
    @Operation(summary = "update skill ")
    public ResponseEntity updatSkill(@RequestBody Skill data, @PathVariable UUID id) {
        Skill result = skillService.update(data, id);
        return responseHandler.response(200, "Successfully update skill ", result);
    }

    @DeleteMapping("skill-group/{id}")
    @Operation(summary = "deleted skill group ")
    public ResponseEntity deletedGroup(@PathVariable UUID id) {
        SkillGroup result = skillService.deleteGroup(id);
        return responseHandler.response(200, "Successfully deleted skill group", result);
    }

    @DeleteMapping("skill/{id}")
    @Operation(summary = "deleted skill group ")
    public ResponseEntity deleted(@PathVariable UUID id) {
        Skill result = skillService.delete(id);
        return responseHandler.response(200, "Successfully deleted skill ", result);
    }

    @PostMapping("skill")
    @Operation(summary = "Create Skill ")
    public ResponseEntity createSkill( @RequestBody Skill skillName, @Parameter String groupId) {
        Skill result = skillService.createSkill(skillName,UUID.fromString( groupId));
        return responseHandler.response(200, "Successfully Create skill ", result);
    }

    @PostMapping("skill-group")
    @Operation(summary = "Create Skill Group ")
    public ResponseEntity createGroup(@Valid @RequestBody SkillGroup skill) {
        SkillGroup result = skillService.createGroup(skill);
        return responseHandler.response(200, "Successfully Create skill group ", result);
    }

    @PostMapping("skill-groups")
    @Operation(summary = "Create Skill Group ")
    public ResponseEntity createGroup(@Valid @RequestBody List<String> skill) {
        List<SkillGroup> result = skillService.createGroups(skill);
        return responseHandler.response(200, "Successfully Create skill Group", result);
    }

    @PostMapping("skills")
    @Operation(summary = "Create list Skill ")
    public ResponseEntity create(@Valid @RequestBody HashMap<String, UUID> skillName) {
        List<Skill> result = skillService.createSkill(skillName);
        return responseHandler.response(200, "Successfully Create skill ", result);
    }

}
