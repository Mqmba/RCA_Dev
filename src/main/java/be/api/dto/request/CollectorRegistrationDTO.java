package be.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectorRegistrationDTO extends UserRegistrationDTO {
    private Boolean isWorking;
}