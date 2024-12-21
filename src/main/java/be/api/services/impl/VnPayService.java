package be.api.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    private final PaymentHistoryServices paymentHistoryServices;

    public VnPayService(PaymentHistoryServices paymentHistoryServices) {
        this.paymentHistoryServices = paymentHistoryServices;
    }

    public String createPaymentUrl(int numberPoint) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TmnCode = "Q0QOWMPZ";
            String vnp_ReturnUrl = "https://clownfish-app-wvth5.ondigitalocean.app/payment/return";
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis()).substring(7);
            String vnp_OrderInfo = "Thanh toán đơn hàng: " + vnp_TxnRef;
            String orderType = "other";
            String vnp_IpAddr = "127.0.0.1";

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(numberPoint * 100000));
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);


            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    hashData.append(fieldName);
                    hashData.append("=");
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if(itr.hasNext()) {
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHashType =  hmacSHA512("GOWK4U7TNXWI21Y20FZGUZN15NOD64PM", hashData.toString());

            queryUrl += "&vnp_SecureHash=" + vnp_SecureHashType;


            paymentHistoryServices.createPaymentHistory(vnp_TxnRef, 1, numberPoint);



            return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + queryUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String createCollectorsPaymentUrl(int numberPoint) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TmnCode = "Q0QOWMPZ";
            String vnp_ReturnUrl = "http://localhost:8080/payment/return-vnpay-collector";
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis()).substring(7);
            String vnp_OrderInfo = "Thanh toán đơn hàng: " + vnp_TxnRef;
            String orderType = "other";
            String vnp_IpAddr = "127.0.0.1";

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(numberPoint * 100000));
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);


            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    hashData.append(fieldName);
                    hashData.append("=");
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if(itr.hasNext()) {
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHashType =  hmacSHA512("GOWK4U7TNXWI21Y20FZGUZN15NOD64PM", hashData.toString());

            queryUrl += "&vnp_SecureHash=" + vnp_SecureHashType;


            paymentHistoryServices.createPaymentHistory(vnp_TxnRef, 1, numberPoint);



            return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + queryUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key hoặc data không được null");
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);

            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAddress = "Invalid IP: " + e.getMessage();
        }
        return ipAddress;
    }
}
