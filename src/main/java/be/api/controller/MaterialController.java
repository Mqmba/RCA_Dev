package be.api.controller;

import be.api.dto.request.MaterialRequestDTO;
import be.api.dto.response.ResponseError;
import be.api.dto.response.ResponseData;
import be.api.dto.request.UserRequestDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.Material;
import be.api.model.User;
import be.api.services.impl.MaterialServices;
import be.api.services.impl.UserServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
@RequiredArgsConstructor
public class MaterialController {


    public final MaterialServices materialServices;

    @PostMapping("create-material")
    public ResponseData<?> createMaterial(@Valid @RequestBody MaterialRequestDTO dto) {
        try{
            Material material = materialServices.addMaterial(dto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Material added successfully", material);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("get-all-material")
    public ResponseData<?> getAllMaterials() {
        try{
            List<Material> materials = materialServices.getAllMaterials();
            return new ResponseData<>(HttpStatus.OK.value(), "Get all materials successfully", materials);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("update-material")
    public ResponseData<?> updateMaterial(@Valid @RequestBody Material dto) {
        try{
            Material material = materialServices.updateMaterial(dto);
            return new ResponseData<>(HttpStatus.OK.value(), "Material updated successfully", material);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @DeleteMapping("delete-material/{id}")
    public ResponseData<?> deleteMaterial(@PathVariable int id) {
        try{
            Boolean result = materialServices.deleteMaterial(id);
            if(result){
                return new ResponseData<>(HttpStatus.OK.value(), "Material deleted successfully", null);
            }
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Material not found");
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

}