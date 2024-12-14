package be.api.controller;

import be.api.dto.request.PointRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.security.anotation.ResidentOrCollectorOnly;
import be.api.services.impl.PointServices;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointServices pointServices;
    private final GenericResponseService responseBuilder;


    @GetMapping("/get-point-by-user")
    public ResponseData<?> getPointByUser() {
        try {
            return new ResponseData<>(200, "Success", pointServices.getPoints());
        } catch (Exception e) {
            return new ResponseData<>(500, "An unexpected error occurred", null);
        }
    }

    @GetMapping("/get-point-of-user")
    public ResponseData<?> getPointOfUser() {
        try {
            return new ResponseData<>(200, "Success", pointServices.getPoints());
        } catch (Exception e) {
            return new ResponseData<>(500, "An unexpected error occurred", null);
        }
    }

    @PostMapping("/update-point-by-user")
    public ResponseData<String> updatePointByUser(@RequestBody PointRequestDTO pointRequest) {
        try{
            return new ResponseData<>(200,"",pointServices.updatePointByUser(pointRequest));
        } catch (Exception e) {
            return new ResponseData<>(500, "An unexpected error occurred:" + e.getMessage(), null);
        }
    }
}
