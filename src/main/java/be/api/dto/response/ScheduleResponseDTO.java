package be.api.dto.response;

import be.api.model.Resident;
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
    private int scheduleId;
    private String materialType;
    private String status;
    private int BuildingId;
    private int RecyclingDepotId;
    private int CollectorId;
    private int ResidentId;
    private Date scheduleDate;
    private Date createdAt;
    private Date updatedAt;

}
