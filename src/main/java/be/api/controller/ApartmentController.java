package be.api.controller;

import be.api.dto.request.ApartmentRequestDTO;
import be.api.model.Apartment;
import be.api.services.impl.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private static final Logger logger = LoggerFactory.getLogger(ApartmentController.class);
    private final ApartmentService apartmentService;

    @PostMapping("/create-apartment")
    public ResponseEntity<?> createApartment(@RequestBody ApartmentRequestDTO apartmentData) {
        try {
            Apartment apartment = apartmentService.createApartment(apartmentData);
            return ResponseEntity.ok(apartment);
        } catch (Exception e) {
            logger.error("Error while creating apartment: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to create apartment: " + e.getMessage());
        }
    }

    @PutMapping("/update-apartment")
    public ResponseEntity<?> updateApartment(@RequestBody ApartmentRequestDTO apartmentData) {
        try {
            Apartment apartment = apartmentService.updateApartment(apartmentData);
            return ResponseEntity.ok(apartment);
        } catch (Exception e) {
            logger.error("Error while updating apartment: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to update apartment: " + e.getMessage());
        }
    }

    @GetMapping("/get-list-apartment-by-paging")
    public ResponseEntity<?> getApartments(@RequestParam int page, @RequestParam int size) {
        try {
            return ResponseEntity.ok(apartmentService.getAllApartments(page, size));
        } catch (Exception e) {
            logger.error("Error while retrieving apartments: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to get apartments: " + e.getMessage());
        }
    }
}