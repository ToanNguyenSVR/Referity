package online.referity.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatCompletion {
    private String id;
    private String object;
    private Long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    // getters and setters
}



