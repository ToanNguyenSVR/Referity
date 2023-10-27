package online.referity.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SkillListRequest {
    UUID id;
    UUID skillLevelId ;
    String name;
    int point;
}
