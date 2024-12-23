package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.services.impl.TransactionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServices transactionServices;

    @PostMapping("/get-transaction-by-user")
    public ResponseData<?> getTransactionByUser() {
        try {
            return new ResponseData<>(200, "Thành công", transactionServices.getListTransactionHistoryByToken());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/get-transaction-by-user-id")
    public ResponseData<?> getTransactionByUserId(@RequestParam int userId) {
        try {
            return new ResponseData<>(200, "Thành công", transactionServices.getListTransactionHistoryByUserId(userId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
