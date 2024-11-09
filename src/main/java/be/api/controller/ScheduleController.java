package be.api.controller;

import be.api.dto.request.ScheduleDTO;
import be.api.dto.response.ResponseData;
import be.api.model.Schedule;
import be.api.services.impl.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/get-schedule")
    public ResponseData<List<Schedule>> getSchedules() {
        try {
            log.info("Retrieving all schedules");
            List<Schedule> schedules = scheduleService.getSchedules();
            return new ResponseData<>(200, "Successfully retrieved schedules", schedules);
        } catch (Exception e) {
            log.error("Error retrieving schedules", e);
            return new ResponseData<>(500, "Internal server error while retrieving schedules", null);
        }
    }

    @PostMapping("/create-schedule")
    public ResponseData<Schedule> createSchedule(@RequestBody ScheduleDTO schedule) {
        try {
            log.info("Creating a new schedule");
            Schedule createdSchedule = scheduleService.addSchedule(schedule);
            return new ResponseData<>(200, "Schedule created successfully", createdSchedule);
        } catch (Exception e) {
            log.error("Error creating schedule", e);
            return new ResponseData<>(500, e.getMessage(), null);
        }
    }
}
