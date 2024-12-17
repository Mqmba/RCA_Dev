package be.api.repository;

import be.api.model.Payment_History;
import be.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPaymentHistoryRepository extends JpaRepository <Payment_History, Integer> {
    Payment_History findByOrderCode(String orderCode);

    List<Payment_History> findByUser(User user);
}
