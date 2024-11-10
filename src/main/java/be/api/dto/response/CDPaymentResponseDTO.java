package be.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CDPaymentResponseDTO {
    private int paymentID;
    private double amount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String status;
}
