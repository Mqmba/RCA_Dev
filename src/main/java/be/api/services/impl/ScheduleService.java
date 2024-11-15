package be.api.services.impl;

import be.api.dto.request.ScheduleDTO;
import be.api.model.Collector;
import be.api.model.Schedule;
import be.api.model.User;
import be.api.repository.*;
import be.api.services.IScheduleService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {
    private final IScheduleRepository scheduleRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final ICollectorRepository collectorRepository;
    private final IBuildingRepository buildingRepository;
    private final IResidentRepository residentRepository;
    private final IUserRepository userRepository;

    @Override
    public Page<Schedule> getAllSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public Page<Schedule> getActiveSchedules(Pageable pageable) {
        return scheduleRepository.findByStatus("ACTIVE", pageable);
    }

    @Override
    public Schedule createSchedule(ScheduleDTO scheduleDTO) {
        Schedule sch = new Schedule();
        sch.setMaterialType(scheduleDTO.materialType());
        sch.setStatus(Schedule.scheduleStatus.PENDING);
        sch.setScheduleDate(scheduleDTO.scheduleDate());
        if (scheduleDTO.buildingId() != null) {
            sch.setBuilding(buildingRepository.findById(scheduleDTO.buildingId())
                    .orElseThrow(() -> new RuntimeException("Building not found with ID: " + scheduleDTO.buildingId())));
        }
        if (scheduleDTO.residentId() != null) {
            sch.setResident(residentRepository.findById(scheduleDTO.residentId())
                    .orElseThrow(() -> new RuntimeException("Resident not found with ID: " + scheduleDTO.residentId())));
        }
        return scheduleRepository.save(sch);
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
    public Schedule changeScheduleStatus(Integer id, Schedule.scheduleStatus status, Integer depotId) {
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

    @Override
    public List<Schedule> getUserSchedules(Integer userId, Schedule.scheduleStatus status) {
        // Find the user associated with the userId and retrieve its collector
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Collector collector = user.getCollector(); // Assuming User has a `Collector` association

        // Fetch schedules for the specific status
        return scheduleRepository.findByCollectorAndStatusIn(collector, List.of(status));
    }

    @Override
    public List<Schedule> getUserSchedules(Integer userId) {
        // Find the user associated with the userId and retrieve its collector
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Collector collector = user.getCollector(); // Assuming User has a `Collector` association

        // Fetch schedules for default statuses
        return scheduleRepository.findByCollectorAndStatusIn(collector,
                List.of(Schedule.scheduleStatus.ONGOING, Schedule.scheduleStatus.ACCEPTED));
    }

}
