package be.api.services;

import be.api.dto.response.ScheduleResponseDTO;
import be.api.model.Collector;
import be.api.model.Schedule;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICollectorServices {
    Page<Collector> getAllCollectors(int page, int size);
    Collector updateCollector(boolean isWorking, int collectorId);
    Schedule acceptCollectSchedule(Integer scheduleId);
    List<Schedule> getSchedulesByStatus(Schedule.scheduleStatus status);
    List<Schedule> getListScheduleByStatus (Schedule.scheduleStatus status);
    List<Schedule> getAllSchedules();
    List<Schedule> getAllScheduleByUser();
    Boolean changeBalanceToPoint(long point);
}
