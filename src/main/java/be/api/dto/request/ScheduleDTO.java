package be.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


import java.util.Date;

@Data
public  class ScheduleDTO implements Serializable {
    Date scheduleDate;
    String materialType;
}