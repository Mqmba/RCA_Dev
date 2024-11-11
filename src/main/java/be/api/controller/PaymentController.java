package be.api.controller;

import be.api.services.IPaymentServices;
import be.api.services.impl.PaymentServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private IPaymentServices paymentServices;

    @PostMapping("/create-payment")
    public String createPayment(HttpServletRequest request, @RequestParam float price) {
        try {
            return paymentServices.payWithVNPAYOnline(request, price);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
