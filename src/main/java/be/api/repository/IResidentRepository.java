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

}