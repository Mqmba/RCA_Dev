package be.api.repository;

import be.api.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer> {
}
