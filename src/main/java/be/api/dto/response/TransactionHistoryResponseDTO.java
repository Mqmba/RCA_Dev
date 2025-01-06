package be.api.dto.response;

import be.api.model.TransactionHistory;
import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionHistoryResponseDTO implements Serializable {
    TransactionHistory transactionHistory;
    String type;


}
