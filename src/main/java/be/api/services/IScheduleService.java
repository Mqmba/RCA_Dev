package be.api.services;

import be.api.dto.request.ScheduleDTO;
import be.api.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IScheduleService {
    Page<Schedule> getAllSchedules(Pageable pageable);
    Page<Schedule> getActiveSchedules(Pageable pageable);
    Schedule createSchedule(ScheduleDTO scheduleDTO);
    Schedule updateSchedule(Integer id, Schedule schedule);
    Schedule changeScheduleStatus(Integer id, String status, Integer depotId);
    Page<Schedule> getUserSchedules(Integer userId, Pageable pageable);
}

