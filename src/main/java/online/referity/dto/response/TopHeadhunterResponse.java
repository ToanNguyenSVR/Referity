package online.referity.dto.response;

import lombok.Data;

import java.util.UUID;
@Data
public class TopHeadhunterResponse {

    private UUID id;

    private String description;

    private String fullName;

    private String avatar;

    private float avg_star ;
}
