package be.api.repository;

import be.api.model.CRPayment_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICRPaymentDetailRepository extends JpaRepository<CRPayment_Detail, Integer> {
    List<CRPayment_Detail> findByCrPaymentId(Integer paymentId);
}
