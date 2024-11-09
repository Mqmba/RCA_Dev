package be.api.dto.response;

import be.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {
    private String scheduleId;
    private Date scheduleDate;
    private Date createAt;
    private Date updateAt;
}
