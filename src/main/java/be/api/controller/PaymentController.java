package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.model.Payment_History;
import be.api.services.impl.PaymentHistoryServices;
import be.api.services.impl.TransactionServices;
import be.api.services.impl.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final VnPayService vnPayService;
    private final PaymentHistoryServices paymentHistoryServices;
    private final TransactionServices transactionServices;


    public PaymentController(VnPayService vnPayService, PaymentHistoryServices
                              paymentHistoryServices, TransactionServices transactionServices) {
        this.vnPayService = vnPayService;

        this.paymentHistoryServices = paymentHistoryServices;
        this.transactionServices = transactionServices;
    }

    @GetMapping("/create-payment-url")
    public ResponseData<?> createPayment(@RequestParam int numberPoint) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(numberPoint);
            return new ResponseData<>(HttpStatus.OK.value(), "Created payment url successfully", paymentUrl);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/return")
    public RedirectView vnPayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            fields.put(fieldName, fieldValue);
        }
        String vnp_ResponseCode = fields.get("vnp_ResponseCode");
        String vnp_txnRef = fields.get("vnp_TxnRef");


        if ("00".equals(vnp_ResponseCode)) {
            paymentHistoryServices.updateSuccessPayment(vnp_txnRef);
            return new RedirectView("https://depot-app.web.app/payment-success");

        }
        return new RedirectView("https://depot-app.web.app/payment-fail");
    }

    @GetMapping("/create-collectors-payment-url")
    public ResponseData<?> createCollectorsPayment(@RequestParam int numberPoint) {
        try {
            String paymentUrl = vnPayService.createCollectorsPaymentUrl(numberPoint);
            return new ResponseData<>(HttpStatus.OK.value(), "Created payment url successfully", paymentUrl);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Created payment url failed");
        }
    }

    @GetMapping("/return-vnpay-collector")
    public RedirectView vnPayReturnCollector(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            fields.put(fieldName, fieldValue);
        }
        String vnp_ResponseCode = fields.get("vnp_ResponseCode");
        String vnp_txnRef = fields.get("vnp_TxnRef");


        if ("00".equals(vnp_ResponseCode)) {
            paymentHistoryServices.updateSuccessPaymentCollector(vnp_txnRef);
            return new RedirectView("https://collector-app.web.app/payment-success");

        }
        return new RedirectView("https://collector-app.web.app/payment-fail");
    }

    @GetMapping("/get-list-payment-by-user")
    public ResponseData<?> getListPayment() {
        try {
            List<Payment_History> list = paymentHistoryServices.getPaymentHistoryByUser();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách thành công", list);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/change-point-from-depot-to-collector")
    public ResponseData<?> changePointFromDepotToCollector(@RequestParam long numberPoint, @RequestParam int collectorId) {
        try {
            paymentHistoryServices.changePointFromDepotToCollector(numberPoint, collectorId);
            return new ResponseData<>(HttpStatus.OK.value(), "Đổi điểm thành công", null);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/change-point-to-user")
    public ResponseData<?> changePointToUser(@RequestParam long numberPoint, @RequestParam int userId) {
        try {
            transactionServices.transferPoint(userId, numberPoint);
            return new ResponseData<>(HttpStatus.OK.value(), "Chuyển tiền thành công", null);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    private String signData(Map<String, String> fields, String secretKey) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(fieldValue);
                hashData.append("&");
            }
        }
        hashData.deleteCharAt(hashData.length() - 1);
        return hmacSHA512(secretKey, hashData.toString());
    }




    private String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hmacData.length);
            for (byte b : hmacData) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}
