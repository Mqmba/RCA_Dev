package be.api.controller;

import be.api.dto.response.ResponseError;
import be.api.dto.response.ResponseData;
import be.api.dto.request.UserRequestDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.User;
import be.api.services.impl.PointServices;
import be.api.services.impl.ResidentServices;
import be.api.services.impl.UserServices;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;
    private final PointServices pointServices;
    private final ResidentServices residentServices;


    @PostMapping("add")
    public ResponseData<?> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
        try{
            int id = userServices.saveUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", id);
        }
        catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PostMapping("get-user-by-id/{id}")
    public ResponseData<?> getUserById(@PathVariable int id) {
        try{
            UserRequestDTO user = userServices.getUserById(id);
            return new ResponseData<>(HttpStatus.OK.value(), "User found", user);
        }
        catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PostMapping("update/{id}")
    public ResponseData<?> updateUser(@PathVariable int id, @RequestBody UserRequestDTO userDTO) {
        try{
            userServices.updateUser(id, userDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "User updated successfully", null);
        }catch (Exception e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PostMapping("get-list-user-by-paging")
    public ResponseData<?> getListUserByPaging(@RequestParam int pageNo, @RequestParam int pageSize) {
        try {
           return new ResponseData<>(HttpStatus.OK.value(), "List user found", userServices.getListUserByPaging(pageNo, pageSize));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @DeleteMapping("delete-user-by-id/{id}")
    public ResponseData<?> deleteUserById(@PathVariable int id) {
        try {
            userServices.deleteUser(id);
            return new ResponseData<>(HttpStatus.OK.value(), "User deleted successfully", null);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("search-users-by-name")
    public ResponseData<?> searchUsersByName(@RequestParam String name, @RequestParam int pageNo, @RequestParam int pageSize) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List user found", userServices.searchUsersByName(name, pageNo, pageSize));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("get-user-by-email")
    public ResponseData<User> getUserByEmail(@RequestParam String email) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "User found", userServices.getUserByEmail(email));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("get-info-user")
    public ResponseData<?> getInfoUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "User found", userServices.getInfoUser());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-point-by-user")
    public ResponseData<?> getPointByUser() {
        try {
            return new ResponseData<>(200, "Lấy thành công", pointServices.getPoints());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @GetMapping("/change-point-to-voucher")
    public ResponseData<?> changePointToVoucher(@RequestParam int voucherId) {
        try {
            return new ResponseData<>(200, "Đổi thành công", residentServices.changePointToVoucher(voucherId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }



    @GetMapping("/get-list-voucher-by-user")
    public ResponseData<?> getListVoucherByUser() {
        try {
            return new ResponseData<>(200, "Success", residentServices.getListVoucherByResidentId());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


}
