package be.api.services.impl;

import be.api.dto.request.ScheduleDTO;
import be.api.model.Schedule;
import be.api.repository.IScheduleRepository;
import be.api.services.IScheduleServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService implements IScheduleServices {

    private final IScheduleRepository scheduleRepository;

//    @Override
//    public Schedule getSchedule(String id) {
//        return scheduleRepository.findById(id); // or throw a custom exception if not found
//    }

    @Override
    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule addSchedule(ScheduleDTO scheduleDTO) {
        Schedule scheduleModify = new Schedule();
        scheduleModify.setScheduleDate(scheduleDTO.getScheduleDate());
        return scheduleRepository.save(scheduleModify);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        return null;
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
    }
}
