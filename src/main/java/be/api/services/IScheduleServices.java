package be.api.services;

import be.api.dto.request.ScheduleDTO;
import be.api.model.Schedule;

import java.util.List;

public interface IScheduleServices {
//    Schedule getSchedule(String id);
    List<Schedule> getSchedules();
    Schedule addSchedule(ScheduleDTO schedule);
    Schedule updateSchedule(Schedule schedule);
    void deleteSchedule(Schedule schedule);
}

