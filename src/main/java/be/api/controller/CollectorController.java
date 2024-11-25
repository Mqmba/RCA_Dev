package be.api.controller;

import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.ScheduleResponseDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.Material;
import be.api.model.Schedule;
import be.api.security.anotation.CollectorOnly;
import be.api.services.impl.CRPaymentServices;
import be.api.services.impl.CollectorServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collector")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorServices collectorServices;
    private final CRPaymentServices crPaymentServices;

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

    @PatchMapping("/accept-colecttion-schedule-by-id")
    @CollectorOnly
    ResponseData<?> acceptCollector(@RequestParam Integer scheduleId) {
        try{
            Schedule schedule = collectorServices.acceptCollectSchedule(scheduleId);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Material added successfully", "Schedule accepted");
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-list-collection-schedule-by-user-by-status")
    @CollectorOnly
    ResponseData<List<Schedule>> getCollectionSchedule(@RequestParam(value = "status", required = false) Schedule.scheduleStatus status,
                                                       @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder)
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
                schedules = collectorServices.getListScheduleByStatus(status);
            }

            schedules.sort((s1, s2) -> {
                if (sortOrder.equalsIgnoreCase("ASC")) {
                    return s1.getCreatedAt().compareTo(s2.getCreatedAt());
                } else {
                    return s2.getCreatedAt().compareTo(s1.getCreatedAt());
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


    @GetMapping("/get-list-collection-schedule-by-status")
    ResponseData<?> getCollectionScheduleByStatus(
            @RequestParam(value = "status", required = false) Schedule.scheduleStatus status,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
        try {
            if (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC")) {
                return new ResponseData<>(400, "Invalid sortOrder parameter. Use 'ASC' or 'DESC'.", null);
            }

            List<Schedule> schedules;
            if (status == null) {
                schedules = collectorServices.getAllSchedules();
            } else {
                schedules = collectorServices.getListScheduleByStatus(status);
            }

            schedules.sort((s1, s2) -> {
                if (sortOrder.equalsIgnoreCase("ASC")) {
                    return s1.getCreatedAt().compareTo(s2.getCreatedAt());
                } else {
                    return s2.getCreatedAt().compareTo(s1.getCreatedAt());
                }
            });

            return new ResponseData<>(200, "Successfully retrieved sorted list of schedules", schedules);
        } catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while retrieving and sorting schedules: " + e.getMessage(), null);
        }
    }

    @PostMapping("/create-collector-resident-payment")
    ResponseData<?> createCollectorResidentPayment(@RequestBody CRPaymentRequestDTO crPaymentRequestDTO) {
        try{
            return new ResponseData<>(200,"create-success", crPaymentServices.createCRPayment(crPaymentRequestDTO));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while crate collect with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("update-successful-collector-resident-payment")
    ResponseData<?> updateSuccessfulPayment(@RequestParam int paymentId) {
        try{
            return new ResponseData<>(200,"update-success", crPaymentServices.updateSuccessCRPayment(paymentId));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while updating payment with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("get-payment-cr-by-id")
    ResponseData<?> getPaymentCRById(@RequestParam int paymentId) {
        try{
            return new ResponseData<>(200,"get-success", crPaymentServices.getCRPaymentById(paymentId));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while getting payment with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("get-payment-cr-by-schedule-id")
    ResponseData<?> getPaymentCRByScheduleId(@RequestParam int scheduleId) {
        try{
            return new ResponseData<>(200,"get-success", crPaymentServices.findByScheduleId(scheduleId));
        }catch (Exception e) {
            return new ResponseData<>(500, "Internal server error while getting payment with message: " + e.getMessage(), null);
        }
    }




}
