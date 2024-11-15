package be.api.controller;

import be.api.dto.request.ScheduleDTO;
import be.api.dto.response.ResponseData;
import be.api.model.Schedule;
import be.api.services.impl.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/get-list-collection-schedule-by-paging")
    public ResponseEntity<Page<Schedule>> getAllSchedules(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getAllSchedules(pageable));
    }

    @GetMapping("/get-list-active-collection-schedule-by-paging")
    public ResponseEntity<Page<Schedule>> getActiveSchedules(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getActiveSchedules(pageable));
    }

    @PostMapping("/create-collection-schedule")
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleDTO scheduleDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleDto));
    }

    @PutMapping("/update-collection-schedule")
    public ResponseEntity<Schedule> updateSchedule(@RequestParam Integer id, @RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, schedule));
    }

    @PutMapping("/change-schedule-status-by-id")
    public ResponseEntity<Schedule> changeScheduleStatus(@RequestParam Integer id,
                                                         @RequestParam Schedule.scheduleStatus status,
                                                         @RequestParam Integer depotId) {
        return ResponseEntity.ok(scheduleService.changeScheduleStatus(id, status, depotId));
    }

    @GetMapping("/get-list-collection-schedule-by-user")
    public ResponseEntity<Page<Schedule>> getUserSchedules(@RequestParam Integer userId, Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getUserSchedules(userId, pageable));
    }
}
