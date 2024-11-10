package be.api.repository;


import be.api.model.CollectorDepotPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CDPaymentRepository extends JpaRepository<CollectorDepotPayment, Integer> {
}
