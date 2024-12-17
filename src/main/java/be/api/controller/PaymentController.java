package be.api.controller;

import be.api.services.IPaymentServices;
import be.api.services.impl.PaymentServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private IPaymentServices paymentServices;

    @PostMapping("/create-payment")
    public String createPayment(HttpServletRequest request, @RequestParam int price) {
        try {
            return paymentServices.payWithVNPAYOnline(request, price);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException, IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        Long paymentId = Long.parseLong(queryParams.get("vnp_OrderInfo"));
        float vnp_Amount = Float.parseFloat(queryParams.get("vnp_Amount"));
        if("00".equals(vnp_ResponseCode)){
            boolean isSuccessful = paymentServices.paymentCallback(paymentId.intValue()); // Giả sử paymentId là kiểu int

            if (isSuccessful) {
                System.out.println(String.format("Response Code: %s, Paymet ID: %d, Amount: %.2f", vnp_ResponseCode, paymentId, vnp_Amount));

                //FE đưa link để redirect
//            response.sendRedirect("https://your-frontend-domain.com/payment-success");
            } else {
//            response.sendRedirect("https://your-frontend-domain.com/payment-failure");
            }
        }
    }
}
