package be.api.dto.response;


import be.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO implements Serializable {
    public int transactionId;
    public double numberPoint;
    public String orderCode;
    public String status;
    public User senderId;
    public User receiverId;
}
