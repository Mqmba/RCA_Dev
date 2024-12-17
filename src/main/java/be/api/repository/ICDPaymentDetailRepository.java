package be.api.repository;

import be.api.model.CDPayment_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICDPaymentDetailRepository extends JpaRepository<CDPayment_Detail, Integer> {
    CDPayment_Detail findByCdPaymentId(Integer cdPaymentId);
}
