package be.api.dto.request;

import lombok.Data;

@Data
public class CDPaymentRequestDTO {
    private int collectorId;
    private int depotId;
    private double amount;
    private String paymentMethod;
}
