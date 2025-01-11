package be.api.dto.response;

import be.api.model.Schedule;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminTransactionResponseDTO implements Serializable {
    public long numberTransaction;
    //  pending là những đơn mà đã  được tạo và đang chờ collector tới xác nhận
    public long numberTransactionPending;

    public long numberTransactionSuccess;

    // đã được xác nhận và lấy
    public long numberTransactionGoing;
    public List<Schedule> top5ScheduleByCreatedAt;
}
