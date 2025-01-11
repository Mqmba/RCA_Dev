package be.api.controller;

import be.api.dto.request.DepotMaterialRequestDTO;
import be.api.dto.request.UserRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.services.impl.DepotMaterialServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/depot-material")
@RequiredArgsConstructor
public class DepotMaterialController {
    private final DepotMaterialServices depotMaterialService;

    @PostMapping("create-update-depot-material")
    public ResponseData<?> addUser(@Valid @RequestBody DepotMaterialRequestDTO dto) {
        try{
            return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo thành công", depotMaterialService.createDepotMaterial(dto));
        }
        catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

}

