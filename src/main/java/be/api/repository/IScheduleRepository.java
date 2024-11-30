package be.api.repository;

import be.api.dto.response.ScheduleResponseDTO;
import be.api.model.Collector;
import be.api.model.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
    Page<Schedule> findByStatus(String status, Pageable pageable);
    Page<Schedule> findByCollector(Integer collectorId, Pageable pageable);
    List<Schedule> findByCollectorAndStatusIn(Collector collectorId, List<Schedule.scheduleStatus> statuses);
    List<Schedule> findByStatusIn(List<Schedule.scheduleStatus> statuses);
    @Query("SELECT s FROM Schedule s WHERE s.status = :status")
    List<Schedule> findByStatus(@Param("status") Schedule.scheduleStatus status);

    @Query("SELECT s FROM Schedule s WHERE s.status = :status AND s.collector.collectorId = :collectorId")
    List<Schedule> findByCollectorAndStatus(@Param("collectorId") int collectorId, @Param("status") Schedule.scheduleStatus status);


    @Query("SELECT s FROM Schedule s WHERE s.collector.collectorId = :collectorId")
    List<Schedule> findByCollector(@Param("collectorId") Integer collectorId);

    @Query("SELECT s FROM Schedule s WHERE s.residentId.residentId = :residentId")
    List<Schedule> findByResidentId(@Param("residentId") Integer residentId);

    @Query("SELECT s FROM Schedule s WHERE s.residentId.residentId = :residentId AND s.status = :status")
    List<Schedule> findByResidentAndStatus(@Param("residentId") Integer residentId, @Param("status") Schedule.scheduleStatus status);

    @Query("SELECT COUNT(s) FROM Schedule s WHERE s.status = :SUCCESS")
    long findByNumberTransaction(
            @Param("SUCCESS") Schedule.scheduleStatus SUCCESS
    );

    @Query("SELECT COUNT(s) FROM Schedule s WHERE s.status = :ACCEPTED")
    long findByAccepteAndOnGoing(
            @Param("ACCEPTED") Schedule.scheduleStatus ACCEPTED
    );
    @Query("Select s FROM Schedule s  ORDER BY s.createdAt DESC")
    List<Schedule> findTop5ByOrderByCreatedAtDesc();

}
