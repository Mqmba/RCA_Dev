package be.api.services.impl;

import be.api.model.Schedule;
import be.api.repository.ICollectorRepository;
import be.api.repository.IRecyclingDepotRepository;
import be.api.repository.IScheduleRepository;
import be.api.services.IScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository scheduleRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final ICollectorRepository collectorRepository;
    @Override
    public Page<Schedule> getAllSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public Page<Schedule> getActiveSchedules(Pageable pageable) {
        return scheduleRepository.findByStatus("ACTIVE", pageable);
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Integer id, Schedule schedule) {
        Optional<Schedule> existingSchedule = scheduleRepository.findById(id);
        if (existingSchedule.isPresent()) {
            Schedule updatedSchedule = existingSchedule.get();
            updatedSchedule.setScheduleDate(schedule.getScheduleDate());
            updatedSchedule.setMaterialType(schedule.getMaterialType());
            updatedSchedule.setBuilding(schedule.getBuilding());
            updatedSchedule.setRecyclingDepot(schedule.getRecyclingDepot());
            return scheduleRepository.save(updatedSchedule);
        }
        return null;
    }

    @Override
    public Schedule changeScheduleStatus(Integer id, String status, Integer depotId) {
        Optional<Schedule> existingSchedule = scheduleRepository.findById(id);
        if (existingSchedule.isPresent()) {
            Schedule schedule = existingSchedule.get();
            schedule.setStatus(status);

            schedule.setRecyclingDepot(recyclingDepotRepository.findById(depotId).orElseThrow(() -> new EntityNotFoundException("RecyclingDepot not found with id: " + depotId)));
            return scheduleRepository.save(schedule);
        }
        return null;
    }

    @Override
    public Page<Schedule> getUserSchedules(Integer userId, Pageable pageable) {
        return scheduleRepository.findByCollector(collectorRepository.findByUserUserId(userId).stream().findFirst().orElseThrow(() -> new EntityNotFoundException("User not found id: " + userId)).getUserId(), pageable);
    }
}
