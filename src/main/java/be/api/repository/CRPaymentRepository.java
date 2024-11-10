package be.api.repository;

import be.api.model.CollectorResidentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CRPaymentRepository extends JpaRepository<CollectorResidentPayment, Integer> {
}
