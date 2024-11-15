package be.api.repository;

import be.api.model.Collector;
import be.api.model.Schedule;
import be.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICollectorRepository extends JpaRepository<Collector, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Collector SET numberPoint = numberPoint - :amount WHERE collectorId = :id")
    void updateByCollectorId(int id, int amount);

    List<User> findByUserUserId(Integer userId);
    List<Collector> findByCollectorId(Integer CollectorId);
}

