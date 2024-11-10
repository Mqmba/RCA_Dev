package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.services.impl.CollectorServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/change-is-working-status")
    ResponseData<?> updateCollectorStatus(@RequestParam boolean isWorking, @RequestParam int collectorId){
        try{
            return new ResponseData<>(200,"update success", collectorServices.updateCollector(isWorking, collectorId));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while updating collector status with message: " + e.getMessage(), null);
        }
    }
}
