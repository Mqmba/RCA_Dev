package be.api.dto.response;

import be.api.model.Schedule;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminTransactionResponseDTO implements Serializable {
    public long numberTransaction;
    public long numberTransactionPending;
    public long numberTransactionSuccess;
    public long numberTransactionGoing;
//    public List<Schedule> top5ScheduleByCreatedAt;
}
