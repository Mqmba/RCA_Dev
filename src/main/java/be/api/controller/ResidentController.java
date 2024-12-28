package be.api.controller;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.dto.request.VoucherRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.model.Resident;
import be.api.services.impl.ResidentServices;
import jakarta.validation.Valid;
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



    @PostMapping("/analyze-material")
    public ResponseData<?> analyzeMaterial() {
        try {
            return new ResponseData<>(200, "Thành công", residentService.analyzeMaterialByResidentId());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}