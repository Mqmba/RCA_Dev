package be.api.dto.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class CancelScheduleRequestDTO implements Serializable {
    private int scheduleId;
    private String reason;
}
