package be.api.dto.response;

import be.api.model.Schedule;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminTransactionResponseDTO {
    public long numberTransaction;
    public long numberTransactionPending;
    public long numberTransactionSuccess;
    public long numberTransactionGoing;
    public List<Schedule> top5ScheduleByCreatedAt;
}
