package online.referity.dto.request;

import lombok.Data;
import online.referity.entity.Skill;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class SkillRegisterRequest {

    @Size(max = 50 , message = "Skill Name must less 50 character")
    private String skillName ;

    @NotBlank(message = "GroupId requested")
    private UUID groupId ;
}
