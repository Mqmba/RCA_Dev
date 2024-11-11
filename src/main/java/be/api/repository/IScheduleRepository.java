package be.api.repository;

import be.api.model.Collector;
import be.api.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findByStatus(String status, Pageable pageable);
    Page<Schedule> findByCollector(Integer collectorId, Pageable pageable);
}
