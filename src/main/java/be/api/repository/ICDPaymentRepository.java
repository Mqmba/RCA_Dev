package be.api.repository;


import be.api.dto.response.ResponseData;
import be.api.model.Collector;
import be.api.model.CollectorDepotPayment;
import be.api.model.RecyclingDepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICDPaymentRepository extends JpaRepository<CollectorDepotPayment, Integer> {
    List<CollectorDepotPayment> findByRecyclingDepot(RecyclingDepot recyclingDepot);
}
