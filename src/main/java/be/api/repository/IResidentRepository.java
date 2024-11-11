package be.api.repository;

import be.api.model.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IResidentRepository extends JpaRepository<Resident, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Resident SET rewardPoints = rewardPoints + :amount WHERE residentId = :id")
    void updateResidentById(int id, int amount);
}