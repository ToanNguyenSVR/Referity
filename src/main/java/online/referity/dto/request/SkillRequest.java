package online.referity.dto.request;

import lombok.Data;

import javax.persistence.Column;

@Data
public class SkillRequest {


    @Column(nullable = false, unique = true)
    private String skillName ;

    @Column(nullable = false)
    private int skillLevel = 1                          ;

    @Column(nullable = false)
    private int point = 1 ;
}
