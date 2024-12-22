package be.api.services;

import be.api.dto.request.ScheduleDTO;
import be.api.dto.response.ScheduleResponseDTO;
import be.api.model.Schedule;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IScheduleService {
    Page<Schedule> getAllSchedules(Pageable pageable);
    Page<Schedule> getActiveSchedules(Pageable pageable);
    Schedule createSchedule(ScheduleDTO scheduleDTO, int userId);
    Schedule updateSchedule(Integer id, Schedule schedule);
    Schedule changeScheduleStatus(Integer id, Schedule.scheduleStatus status, Integer depotId);
    Page<Schedule> getUserSchedules(Integer userId, Pageable pageable);
    List<Schedule> getUserSchedules(Integer userId,Schedule.scheduleStatus status);
    List<Schedule> getUserSchedules(Integer userId);
    Schedule getScheduleById(Integer id);

    @Query("SELECT s FROM Schedule s WHERE s.residentId = :residentId AND s.status = :status")
    List<Schedule> getScheduleOfResidentByUserIdAndStatus(Integer residentId, Schedule.scheduleStatus status);


}

