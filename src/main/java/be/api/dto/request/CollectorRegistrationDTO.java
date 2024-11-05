package be.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectorRegistrationDTO extends UserRegistrationDTO {
    private Integer rate;
    private Integer numberPoint;
    private Boolean isWorking;
}