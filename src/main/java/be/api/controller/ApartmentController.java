package be.api.controller;

import be.api.dto.request.ApartmentRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.model.Apartment;
import be.api.services.impl.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private static final Logger logger = LoggerFactory.getLogger(ApartmentController.class);
    private final ApartmentService apartmentService;

    @PostMapping("/create-apartment")
    public ResponseData<?> createApartment(@RequestBody ApartmentRequestDTO apartmentData) {
        try {
            Apartment apartment = apartmentService.createApartment(apartmentData);
            return new ResponseData<>(200, "Tạo thành công", apartment);
        } catch (Exception e) {
            logger.error("Error while creating apartment: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/update-apartment")
    public ResponseData<?> updateApartment(@RequestBody ApartmentRequestDTO apartmentData) {
        try {
            Apartment apartment = apartmentService.updateApartment(apartmentData);
            return new ResponseData<>(200, "Tạo thành công", apartment);
        } catch (Exception e) {
            logger.error("Error while updating apartment: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-list-apartment")
    public ResponseData<?> getApartments() {
        try {
            return new ResponseData<>(200, "Lấy thành công", apartmentService.getAllApartments());
        } catch (Exception e) {
            logger.error("Error while retrieving apartments: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-list-apartment-by-building-id")
    public ResponseData<?> getApartmentsByBuildingId(@RequestParam int buildingId) {
        try {
            return new ResponseData<>(200, "Lấy thành công", apartmentService.getListApartmentByBuildingId(buildingId));
        } catch (Exception e) {
            logger.error("Error while retrieving apartments: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

}