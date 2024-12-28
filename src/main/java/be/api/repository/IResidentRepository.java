package be.api.repository;

import be.api.model.Apartment;
import be.api.model.Building;
import be.api.model.Resident;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IResidentRepository extends JpaRepository<Resident, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Resident SET rewardPoints = rewardPoints + :amount WHERE residentId = :id")
    void updateResidentById(int id, int amount);

    Optional<Resident> findByUser_UserId(int userId);

    @Query("SELECT a.building.buildingId " +
            "FROM Resident r " +
            "JOIN r.apartment a " +
            "WHERE r.user.userId = :userId")
    Optional<Integer> findBuildingIdByUserId(@Param("userId") int userId);

    @Query("SELECT r FROM Resident r ORDER BY r.rewardPoints DESC")
    List<Resident> findTop5ByOrderByRewardPointsDesc();

    @Query(value = "SELECT mt.Name AS MaterialTypeName, SUM(md.Quantity) AS TotalWeight " +
            "FROM CRPayment_Detail md " +
            "JOIN Material m ON md.MaterialId = m.MaterialId " +
            "JOIN MaterialType mt ON m.MaterialTypeId = mt.MaterialTypeId " +
            "JOIN CollectorResident_Payment crp ON md.CRPaymentId = crp.CRPaymentId " +
            "JOIN Schedule s ON crp.ScheduleId = s.ScheduleId " +
            "WHERE crp.PaymentStatus = 2 AND s.ResidentId = :residentId " +
            "GROUP BY mt.Name", nativeQuery = true)
    List<Object[]> findAnalyzeMaterialByResidentId(@Param("residentId") int residentId);

    @Query(value =
            "SELECT r.ResidentId, " +
                    "       SUM(md.Quantity) AS TotalWeight, " +
                    "       FIND_IN_SET( " +
                    "           CAST(SUM(md.Quantity) AS CHAR), " +
                    "           ( " +
                    "               SELECT GROUP_CONCAT(TotalWeight ORDER BY TotalWeight DESC) " +
                    "               FROM ( " +
                    "                   SELECT r2.ResidentId, SUM(md2.Quantity) AS TotalWeight " +
                    "                   FROM CRPayment_Detail md2 " +
                    "                   JOIN CollectorResident_Payment crp2 ON md2.CRPaymentId = crp2.CRPaymentId " +
                    "                   JOIN Schedule s2 ON crp2.ScheduleId = s2.ScheduleId " +
                    "                   JOIN Resident r2 ON s2.ResidentId = r2.ResidentId " +
                    "                   WHERE crp2.PaymentStatus = 2 " +
                    "                   GROUP BY r2.ResidentId " +
                    "               ) AS RankedSubquery " +
                    "           ) " +
                    "       ) AS Ranking " +
                    "FROM CRPayment_Detail md " +
                    "JOIN CollectorResident_Payment crp ON md.CRPaymentId = crp.CRPaymentId " +
                    "JOIN Schedule s ON crp.ScheduleId = s.ScheduleId " +
                    "JOIN Resident r ON s.ResidentId = r.ResidentId " +
                    "WHERE crp.PaymentStatus = 2 AND r.ResidentId = :residentId " +
                    "GROUP BY r.ResidentId",
            nativeQuery = true)
    List<Object[]> findRankingByResidentId(@Param("residentId") int residentId);
}