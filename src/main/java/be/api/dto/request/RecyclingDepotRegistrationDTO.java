package be.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecyclingDepotRegistrationDTO extends UserRegistrationDTO {
    private String depotName;
    private String location;
    private Boolean isWorking;
}