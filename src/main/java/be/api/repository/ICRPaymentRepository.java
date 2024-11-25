package be.api.repository;

import be.api.dto.response.CRPaymentResponse;
import be.api.model.CollectorResidentPayment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICRPaymentRepository extends JpaRepository<CollectorResidentPayment, Integer> {
    @Query("SELECT crp FROM CollectorResidentPayment crp WHERE crp.schedule.scheduleId = :scheduleId")
    CollectorResidentPayment findByScheduleId(@Param("scheduleId") Integer scheduleId);
}
