package be.api.controller;

import be.api.dto.request.PointRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.security.anotation.ResidentOrCollectorOnly;
import be.api.services.impl.PointServices;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
    private final PointServices pointServices;
    private final GenericResponseService responseBuilder;

    @GetMapping("/public/point/get-point-by-user")
    @ResidentOrCollectorOnly
    public ResponseData<Integer> getPointByUser() {
        try {
            int points = pointServices.getPoints();
            return new ResponseData<>(200, "Success", points);
        } catch (Exception e) {
            return new ResponseData<>(500, "An unexpected error occurred", null);
        }
    }

    @PostMapping("/collector/point/update-point-by-user")
    public ResponseData<String> updatePointByUser(@RequestBody PointRequestDTO pointRequest) {
        try{
            return new ResponseData<>(200,"",pointServices.updatePointByUser(pointRequest));
        } catch (Exception e) {
            return new ResponseData<>(500, "An unexpected error occurred:" + e.getMessage(), null);
        }
    }
}
