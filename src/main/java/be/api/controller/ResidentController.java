package be.api.controller;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.dto.response.ResponseData;
import be.api.model.Resident;
import be.api.services.impl.ResidentServices;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/residents")
@RequiredArgsConstructor
public class ResidentController {

    private final ResidentServices residentService;
    private static final Logger logger = LoggerFactory.getLogger(ResidentController.class);

    @GetMapping("/get-list-residents-by-paging")
    public ResponseEntity<?> getResidents(@RequestParam int page, @RequestParam int size) {
        try {
            return ResponseEntity.ok(residentService.getPaginateResident(page, size));
        } catch (Exception e) {
            logger.error("Error fetching residents by paging: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(500, "Failed to fetch residents", null));
        }
    }

    @PostMapping("/create-resident")
    public ResponseEntity<?> createResident(@RequestBody ResidentRegistrationDTO resident) {
        try {
            Resident residentCreate = residentService.createResident(resident);
            return ResponseEntity.ok(new ResponseData<>(200, "Create success", residentCreate));
        } catch (Exception e) {
            logger.error("Error creating resident: {}", e.getMessage());
            logger.info("Resident Data: {}", resident);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(500, "Failed to create resident", null));
        }
    }
}