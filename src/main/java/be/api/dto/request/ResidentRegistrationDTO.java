package be.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResidentRegistrationDTO extends UserRegistrationDTO {
    private String residentCode;
}