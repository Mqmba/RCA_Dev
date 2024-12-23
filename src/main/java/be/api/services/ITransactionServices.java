package be.api.services;

import be.api.model.TransactionHistory;

import java.util.List;

public interface ITransactionServices {
    Boolean transferPoint(int receiverId, double numberPoint);
    List<TransactionHistory> getListTransactionHistoryByUserId(int userId);
    List<TransactionHistory> getListTransactionHistoryByToken();

}

