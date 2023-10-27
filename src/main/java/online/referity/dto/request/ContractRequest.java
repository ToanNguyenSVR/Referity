package online.referity.dto.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;

@Data
public class ContractRequest {


    @Size(max = 100, message = "Contract number must not exceed 100 characters")
    private String contractNumber;

    private String contractUrl ;

    private int mothOfContract;



}
