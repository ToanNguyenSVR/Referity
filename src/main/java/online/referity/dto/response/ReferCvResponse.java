package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Setter
@Getter
public class ReferCvResponse {

    @Column(nullable = false)
    private String cvUrl ;

    @Column(nullable = false )
    private String linkedInLink ;

    @Column(nullable = false )
    private String facebookLink ;

    @Column(nullable = false )
    private String githubLink ;

    private String summary ;

    private String education ;
}
