package online.referity.dto;

import lombok.Data;

@Data
public class FcmNotification {
    private String title;
    private String body;
    private String token;
}
