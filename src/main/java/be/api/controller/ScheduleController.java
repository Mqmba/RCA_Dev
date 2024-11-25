package be.api.controller;

import be.api.dto.request.ScheduleDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.model.Schedule;
import be.api.model.User;
import be.api.repository.IUserRepository;
import be.api.security.JwtTokenUtil;
import be.api.security.anotation.ResidentOrCollectorOnly;
import be.api.services.impl.CollectorServices;
import be.api.services.impl.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CollectorServices collectorServices;

    @GetMapping("/get-list-collection-schedule-by-paging")
    public ResponseEntity<Page<Schedule>> getAllSchedules(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getAllSchedules(pageable));
    }

    @GetMapping("/get-list-active-collection-schedule-by-paging")
    public ResponseEntity<Page<Schedule>> getActiveSchedules(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.getActiveSchedules(pageable));
    }

    @PostMapping("/create-collection-schedule")
    public ResponseData<?> createSchedule(HttpServletRequest request, @RequestBody ScheduleDTO scheduleDto) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.replace("Bearer ", "");
                Map<String, Object> tokenInfo = jwtTokenUtil.getTokenInfo(token);
                System.out.println("User ID: " + tokenInfo.get("id"));
                int userId = (int) tokenInfo.get("id");
                Schedule schedule = scheduleService.createSchedule(scheduleDto, userId);
                return new ResponseData<>(HttpStatus.CREATED.value(), "Create Successful");
            } else {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Authorization header is missing or invalid");
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
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
    public ResponseData<List<Schedule>> getUserSchedules(HttpServletRequest request, Schedule.scheduleStatus status) {
        try{
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.replace("Bearer ", "");
                Map<String, Object> tokenInfo = jwtTokenUtil.getTokenInfo(token);
                System.out.println("User ID: " + tokenInfo.get("id"));
                int userId = (int) tokenInfo.get("id");
                List<Schedule> data = scheduleService.getScheduleOfResidentByUserIdAndStatus(userId, status);

                return new ResponseData<>(HttpStatus.CREATED.value(), "Get schedule successful", data);
            } else {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Authorization header is missing or invalid");
            }

        } catch (ResourceNotFoundException e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-list-collection-schedule-by-user-by-status")
    @ResidentOrCollectorOnly
    ResponseData<List<Schedule>> getCollectionSchedule(@RequestParam(value = "status", required = false) Schedule.scheduleStatus status,
                                                       @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder)
    {
        try{

            if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
                return new ResponseData<>(400, "Invalid sortOrder parameter. Use 'ASC' or 'DESC'.", null);
            }

            List<Schedule> schedules;

            if(status == null) {
                schedules = collectorServices.getAllScheduleByUser();
            }
            else {
                schedules = collectorServices.getSchedulesByStatus(status);
            }

            schedules.sort((s1, s2) -> {
                if (sortOrder.equalsIgnoreCase("ASC")) {
                    return s1.getScheduleDate().compareTo(s2.getScheduleDate());
                } else {
                    return s2.getScheduleDate().compareTo(s1.getScheduleDate());
                }
            });

            return new ResponseData<>(
                    200,
                    "Successfully retrieved list collector",
                    schedules);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }



    @GetMapping("/get-schedule-by-id")
    public ResponseData<Schedule> getScheduleById(@RequestParam Integer id) {
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get schedule successful", schedule);
        } catch (ResourceNotFoundException e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
