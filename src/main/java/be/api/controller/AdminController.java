package be.api.controller;

import be.api.dto.request.UserRequestDTO;
import be.api.dto.response.AdminDashboardResponseDTO;
import be.api.dto.response.AdminTransactionResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.services.impl.AdminServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServices adminServices;

    @GetMapping("/dashboard")
    public ResponseData<?> getAdminDashboard() {
        try {
            AdminDashboardResponseDTO adminDashboardResponseDTO = adminServices.getAdminDashBoard();
            return new ResponseData<>(HttpStatus.OK.value(), "Admin dashboard found", adminDashboardResponseDTO);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/all-user")
    public ResponseData<?> getAllUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List user found", adminServices.getAllUser());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/all-depot")
    public ResponseData<?> getAllDepot() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List depot found", adminServices.getAllDepot());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/all-collector")
    public ResponseData<?> getCollector() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List collector found", adminServices.getAllCollector());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-admin-activity")
    public ResponseData<?> getActivityUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "List activity user found", adminServices.getAdminActivity());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/get-admin-transactions")
    public ResponseData<?> getTransactions() {
        try {
            AdminTransactionResponseDTO adminTransactionResponseDTO = adminServices.getAdminTransaction();
            return new ResponseData<>(HttpStatus.OK.value(), "List transactions found", adminTransactionResponseDTO);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

//    @PostMapping("add")
//    public ResponseData<?> addUser(@Valid @RequestBody UserRequestDTO userDTO) {
//        try{
//            int id = userServices.saveUser(userDTO);
//            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully", id);
//        }
//        catch (ResourceNotFoundException e){
//            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
//        }
//    }
}
