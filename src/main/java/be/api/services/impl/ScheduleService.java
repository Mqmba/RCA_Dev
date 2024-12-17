package be.api.services.impl;

import be.api.dto.request.ScheduleDTO;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.IScheduleService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final IApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Schedule> getAllSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

    @Override
    public Page<Schedule> getActiveSchedules(Pageable pageable) {
        return scheduleRepository.findByStatus("ACTIVE", pageable);
    }

    @Override
    public Schedule createSchedule(ScheduleDTO scheduleDTO, int userId) {
     try{
         Schedule model = new Schedule();
         model.setMaterialType(scheduleDTO.getMaterialType());
         model.setScheduleDate(scheduleDTO.getScheduleDate());

         model.setStatus(Schedule.scheduleStatus.PENDING);
         Optional<Integer> buildingId = residentRepository.findBuildingIdByUserId(userId);
         if (buildingId.isPresent()) {
             Building building = buildingRepository.findById(buildingId.get())
                     .orElseThrow(() -> new EntityNotFoundException("Building not found with ID: " + buildingId.get()));
             model.setBuilding(building);

         }
         model.setMaterialType(scheduleDTO.getMaterialType());

         Resident resident = residentRepository.findByUser_UserId(userId)
                 .orElseThrow(() -> new EntityNotFoundException("Resident not found with ID: " + userId));
         model.setResidentId(resident);


         return scheduleRepository.save(model);
     }
     catch (Exception e){
            log.error("Error creating schedule", e);
            throw e;
     }
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Collector collector = user.getCollector();

        List<Schedule> schedules = scheduleRepository.findByCollectorAndStatus(collector.getCollectorId(), status);
        return schedules;
    }

    @Override
    public List<Schedule> getUserSchedules(Integer userId) {
        // Find the user associated with the userId and retrieve its collector
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Collector collector = user.getCollector(); // Assuming User has a `Collector` association

        // Fetch schedules for default statuses
        return scheduleRepository.findByCollectorAndStatusIn(collector,
                List.of(Schedule.scheduleStatus.ACCEPTED));
    }




    @Override
    public Schedule getScheduleById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
    }

    @Override
    public List<Schedule> getScheduleOfResidentByUserIdAndStatus(Integer userId, Schedule.scheduleStatus status) {
       try{
           Resident resident = residentRepository.findByUser_UserId(userId)
                   .orElseThrow(() -> new EntityNotFoundException("Resident not found with id: " + userId));
           Integer residentId = resident.getResidentId();
           return scheduleRepository.findByResidentAndStatus(residentId, status);
       }
       catch (Exception e){
              log.error("Error getting schedule of resident by user id and status", e);
              throw e;
       }
    }

}
