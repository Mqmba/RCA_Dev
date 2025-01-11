package be.api.controller;

import be.api.dto.request.VoucherRequestDTO;
import be.api.dto.response.ResponseError;
import be.api.dto.response.ResponseData;
import be.api.dto.request.UserRequestDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.User;
import be.api.model.Voucher;
import be.api.services.impl.PointServices;
import be.api.services.impl.UserServices;
import be.api.services.impl.VoucherServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final UserServices userServices;
    private final PointServices pointServices;
    private final VoucherServices voucherServices;


    @PostMapping("create-voucher")
    public ResponseData<?> addUser(@Valid @RequestBody VoucherRequestDTO dto) {
        try{
            return new ResponseData<>(HttpStatus.CREATED.value(), "Added successfully", voucherServices.createVoucher(dto));
        }
        catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-all-voucher")
    public ResponseData<?>getAllVouchers() {
        try{
            List<Voucher> vouchers = voucherServices.getListVoucher();
            return new ResponseData<>(200, "List of vouchers found", vouchers);
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list of vouchers with message: " + e.getMessage(), null);
        }
    }

    @GetMapping("/get-voucher-by-id")
    public ResponseData<?> getVoucherById(@RequestParam Integer id) {
        try{
            Voucher voucher = voucherServices.getVoucherById(id);
            return new ResponseData<>(200, "Voucher found", voucher);
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving voucher with message: " + e.getMessage(), null);
        }
    }

}
