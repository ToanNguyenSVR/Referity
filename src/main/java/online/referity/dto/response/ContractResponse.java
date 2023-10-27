package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.CompanyStatus;

import java.sql.Date;
@Data
public class ContractResponse {

    private String name;
    private String shortName;
    private String websiteUrl;
    private String logoUrl;
    private CompanyStatus status ;
    private String contractNumber ;

    private String contractUrl ;

    private Date expireDate ;

    private Date createDate ;



}
