package online.referity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CompanyAddInformationRequest {
    @NotEmpty(message = "Title cannot be empty")
    @Size(max = 100, message = "Title length cannot exceed 100 characters")
    private String title;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 100, message = "Name length cannot exceed 100 characters")
    private String name;

    private String taxCode ;

    @NotEmpty(message = "Short name cannot be empty")
    @Size(max = 50, message = "Short name length cannot exceed 50 characters")
    private String shortName;

    @Pattern(regexp = "^https?://.+", message = "Invalid website URL format")
    private String websiteUrl;

    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 500, message = "Description length cannot exceed 500 characters")
    private String description;

    @Pattern(regexp = "^https?://.+", message = "Invalid website URL format")
    private String logoUrl;

    @NotNull(message = "Establish date cannot be null")
    private LocalDate establishDate;

    @Pattern(regexp = "\\d{10,12}", message = "Beneficiary account must be 10 to 12 digits")
    private String beneficiaryAccount;

    @Pattern(regexp = "[A-Za-z\\s]+", message = "Beneficiary name must contain only letters and spaces")
    private String beneficiaryName;

    @Pattern(regexp = "[A-Za-z\\s]+", message = "Beneficiary bank must contain only letters and spaces")
    private String beneficiaryBank;

}
