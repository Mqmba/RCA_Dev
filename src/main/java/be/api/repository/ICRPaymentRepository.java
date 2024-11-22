package be.api.repository;

import be.api.model.CollectorResidentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICRPaymentRepository extends JpaRepository<CollectorResidentPayment, Integer> {
}
