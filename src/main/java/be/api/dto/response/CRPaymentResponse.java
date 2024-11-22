package be.api.dto.response;

import be.api.model.CRPayment_Detail;
import be.api.model.CollectorResidentPayment;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CRPaymentResponse {
    CollectorResidentPayment payment;
    List<CRPayment_Detail> paymentDetails;
}
