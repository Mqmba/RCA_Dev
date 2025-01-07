package be.api.repository;

import be.api.model.TransactionHistory;
import be.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionHistory extends JpaRepository<TransactionHistory, Integer> {
    List<TransactionHistory> findByUser_UserId(int userId);

    List<TransactionHistory> findByReceiverId(User receiverId);
}

