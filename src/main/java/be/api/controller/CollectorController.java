package be.api.controller;

import be.api.dto.request.CollectorRegistrationDTO;
import be.api.dto.response.ResponseData;
import be.api.model.Collector;
import be.api.model.Schedule;
import be.api.security.anotation.AdminOnly;
import be.api.security.anotation.CollectorOnly;
import be.api.services.impl.CollectorServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collector")
@RequiredArgsConstructor
public class CollectorController {
    private final CollectorServices collectorServices;
    @GetMapping("/get-list-collector-by-paging")
    ResponseData<?> getPaginateCollector(@RequestParam int page, @RequestParam int size) {
        try{
            return new ResponseData<>(
                    200,
                    "Successfully retrieved list collector",
                    collectorServices.getAllCollectors(page, size));
        } catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while retrieving list collector with message: " + e.getMessage(), null);
        }
    }
    @PatchMapping("/change-is-working-status")
    ResponseData<?> updateCollectorStatus(@RequestParam boolean isWorking, @RequestParam int collectorId){
        try{
            return new ResponseData<>(200,"update success", collectorServices.updateCollector(isWorking, collectorId));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while updating collector status with message: " + e.getMessage(), null);
        }
    }

    @PatchMapping("/accept-collection-schedule-by-id")
    @CollectorOnly
    ResponseEntity<Schedule> acceptCollector(@RequestParam Integer scheduleId) {
        return ResponseEntity.ok(collectorServices.acceptCollectSchedule(scheduleId));
    }

    @GetMapping("/get-list-collection-schedule-by-status")
    @CollectorOnly
    ResponseEntity<List<Schedule>> getCollectionSchedule(@RequestParam Schedule.scheduleStatus status) {
        return ResponseEntity.ok(collectorServices.getSchedulesByStatus(status));
    }

    @PostMapping("/create-collector")
    @AdminOnly
    ResponseEntity<Collector> createCollector(@RequestBody CollectorRegistrationDTO collector) {
        return ResponseEntity.ok(collectorServices.createCollector(collector));
    }
}
