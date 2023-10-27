package online.referity.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RegistCampus {

    private  String name  ;


    private String address ;

    private UUID cityId;
}
