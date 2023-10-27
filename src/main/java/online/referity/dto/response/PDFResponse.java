package online.referity.dto.response;

import lombok.Data;

@Data
public class PDFResponse {
    String fullName;
    String email;
    String phone;
    String address;
    String YearOfExperience;
    String summary;
    String skills;
    String education;
    String gpa;
    String jobTitle;

    private String githubLink;
    private String facebookLink;
    private String linkedInLink;
}
