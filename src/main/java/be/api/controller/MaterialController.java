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
            return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo rác thành công", material);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("get-all-material")
    public ResponseData<?> getAllMaterials() {
        try{
            List<Material> materials = materialServices.getAllMaterials();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách thành công", materials);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("update-material")
    public ResponseData<?> updateMaterial(@Valid @RequestBody Material dto) {
        try{
            Material material = materialServices.updateMaterial(dto);
            return new ResponseData<>(HttpStatus.OK.value(), "Update thành công", material);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("delete-material/{id}")
    public ResponseData<?> deleteMaterial(@PathVariable int id) {
        try{
            Boolean result = materialServices.deleteMaterial(id);
            if(result){
                return new ResponseData<>(HttpStatus.OK.value(), "Xóa thành công", null);
            }
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Xóa thất bại");
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

}