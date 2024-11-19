package be.api.services.impl;

import be.api.config.VNPayConfig;
import be.api.infrastructure.AuthenticateJwt;
import be.api.model.Collector;
import be.api.model.CollectorDepotPayment;
import be.api.repository.CDPaymentRepository;
import be.api.repository.ICollectorRepository;
import be.api.services.IPaymentServices;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class PaymentServices implements IPaymentServices {
    @Autowired
    private ICollectorRepository collectorRepository;
    @Autowired
    private CDPaymentRepository cdPaymentRepository;

    @Override
    public String payWithVNPAYOnline(HttpServletRequest request, int price) throws UnsupportedEncodingException {
        int userId = AuthenticateJwt.getCurrentUserId();
        Collector collector = collectorRepository.findByUser_UserId(userId);
        CollectorDepotPayment collectorDepotPayment = new CollectorDepotPayment();
        collectorDepotPayment.setAmount(price);
        collectorDepotPayment.setCollector(collector);
        collectorDepotPayment.setStatus(1);
        cdPaymentRepository.save(collectorDepotPayment);
        float vnp_Amount = price;

        float deposit = (float) (vnp_Amount);
        int vnp_Deposit = (int)deposit;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 10);

        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(String.valueOf(vnp_Deposit) + "00"));
//        vnp_Params.put("vnp_BankCode", VNPayConfig.vnp_BankCode);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", VNPayConfig.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_Locale", VNPayConfig.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", String.valueOf(collectorDepotPayment.getCdPaymentId()));
        vnp_Params.put("vnp_OrderType", String.valueOf("Thanh toán"));
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", "HD" + RandomStringUtils.randomNumeric(6) + "-" + vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr = fieldList.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @Override
    public Boolean paymentCallback(int paymentId) {
        Optional<CollectorDepotPayment> collectorDepotPayment = cdPaymentRepository.findById(paymentId);
        collectorDepotPayment.ifPresent(depotPayment -> depotPayment.setStatus(2));
        cdPaymentRepository.save(collectorDepotPayment.get());
        return true;
    }
}
