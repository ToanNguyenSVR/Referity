package online.referity.dto.response;

import lombok.Data;

@Data
public class Choice {
    private Message message;
    private String finish_reason;
    private Integer index;

    // getters and setters
}
