package be.api.repository;

import be.api.model.RecyclingDepot;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRecyclingDepotRepository extends JpaRepository<RecyclingDepot, Integer> {
    List<RecyclingDepot> findByIsWorkingTrue();

    RecyclingDepot findByUser_Username(String username);


    @Query(value = """
        SELECT 
            mt.Name AS LoaiRac,
            SUM(cdpd.Quantity) AS TongSoKG
        FROM CollectorDepot_Payment cdp
        JOIN CDPayment_Detail cdpd 
            ON cdp.CollectorDepotPaymentId = cdpd.CDPaymentId
        JOIN Material m
            ON cdpd.MaterialId = m.MaterialId
        JOIN MaterialType mt
            ON m.MaterialTypeId = mt.MaterialTypeId
        WHERE cdp.RecyclingDepotId = :depotId
          AND cdp.Status = 'SUCCESS'
        GROUP BY mt.Name
        """, nativeQuery = true)
    List<Object[]> findAnalyzMaterialByDepotId(@Param("depotId") int depotId);
}
