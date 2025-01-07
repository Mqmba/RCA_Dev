package be.api.services;

import be.api.dto.response.TransactionHistoryResponseDTO;
import be.api.model.TransactionHistory;

import java.util.List;

public interface ITransactionServices {
    Boolean transferPoint(int receiverId, double numberPoint);
    List<TransactionHistoryResponseDTO> getListTransactionHistoryByUserId(int userId);
    List<TransactionHistoryResponseDTO> getListTransactionHistoryByToken();

}

