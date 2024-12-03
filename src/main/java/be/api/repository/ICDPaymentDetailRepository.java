package be.api.repository;

import be.api.model.CDPayment_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICDPaymentDetailRepository extends JpaRepository<CDPayment_Detail, Integer> {
    List<CDPayment_Detail> findByCdPaymentId(Integer cdPaymentId);
}
