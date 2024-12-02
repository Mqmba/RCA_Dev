package be.api.services;

import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CDPaymentResponse;
import be.api.dto.response.CRPaymentResponse;
import be.api.dto.response.ResponseData;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICDPaymentServices {
    Integer createCDPayment(CDPaymentRequestDTO dto);

    Boolean updateSuccessCDPayment(Integer paymentId);

    CRPaymentResponse getCDPaymentById(Integer paymentId);

    List<CDPaymentResponse> getListPayment();


}
