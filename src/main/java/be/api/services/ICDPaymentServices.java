package be.api.services;

import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CRPaymentResponse;

public interface ICDPaymentServices {
    Integer createCDPayment(CDPaymentRequestDTO dto);

    Boolean updateSuccessCDPayment(Integer paymentId);

    CRPaymentResponse getCDPaymentById(Integer paymentId);



}
