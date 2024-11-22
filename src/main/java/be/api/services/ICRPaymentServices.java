package be.api.services;

import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CRPaymentResponse;
import be.api.model.CRPayment_Detail;
import be.api.model.CollectorResidentPayment;

public interface ICRPaymentServices {
    Integer createCRPayment(CRPaymentRequestDTO dto);

    Boolean updateSuccessCRPayment(Integer paymentId);

    CRPaymentResponse getCRPaymentById(Integer paymentId);

}
