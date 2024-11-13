package be.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


import java.util.Date;

public record ScheduleDTO(
        Date scheduleDate,
        String materialType,
        Integer buildingId,
        Integer residentId
) {}
