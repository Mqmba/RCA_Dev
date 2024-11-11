package be.api.repository;

import be.api.model.Collector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ICollectorRepository extends JpaRepository<Collector, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Collector SET numberPoint = numberPoint - :amount WHERE collectorId = :id")
    void updateByCollectorId(int id, int amount);
}

