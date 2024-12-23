package be.api.controller;

import be.api.dto.request.DepotMaterialRequestDTO;
import be.api.dto.request.DrawMoneyRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.model.DrawMoneyHistory;
import be.api.services.impl.DrawMoneyServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draw-money")
@RequiredArgsConstructor
public class DrawMoneyController {
    private final DrawMoneyServices drawMoneyServices;

    @PostMapping("/create-draw-money-form")
    public ResponseData<?> createFormDrawMoney(@Valid @RequestBody DrawMoneyRequestDTO dto) {
        try{
            return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo thành công", drawMoneyServices.createDrawMoneyRequest(dto));
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PostMapping("/update-status-draw-money")
    public ResponseData<?> updateStatusDrawMoney(@RequestParam int id, @RequestParam DrawMoneyHistory.STATUS status) {
        try{
            drawMoneyServices.setStatusDrawMoneyRequest(id, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật thành công", null);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PostMapping("/get-draw-money-by-user")
    public ResponseData<?> getDrawMoneyBy() {
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Thành công", drawMoneyServices.getListDrawMoneyRequestByUser());
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }
}
