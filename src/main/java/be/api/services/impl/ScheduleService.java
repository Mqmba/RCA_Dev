package be.api.services.impl;

import be.api.dto.request.CancelScheduleRequestDTO;
import be.api.dto.request.ScheduleDTO;
import be.api.dto.response.ScheduleResponseDTO;
import be.api.exception.BadRequestException;
import be.api.exception.ResourceNotFoundException;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new BadRequestException("Không tìm thấy user:" + userId));
         if(user.getRole() != User.UserRole.ROLE_RESIDENT){
             throw new BadRequestException("Chỉ cư dân mới có thể tạo lịch");
         }

         Schedule existingSchedule = scheduleRepository.findByResidentId(user.getResident().getResidentId())
                 .stream()
                 .filter(schedule -> schedule.getStatus() == Schedule.scheduleStatus.PENDING)
                 .findFirst()
                 .orElse(null);

         if(existingSchedule != null){
                throw new BadRequestException("Bạn đã có lịch đang chờ xác nhận");
         }

         Schedule model = new Schedule();
         model.setMaterialType(scheduleDTO.getMaterialType());
         model.setScheduleDate(scheduleDTO.getScheduleDate());

         model.setStatus(Schedule.scheduleStatus.PENDING);
         Optional<Integer> buildingId = residentRepository.findBuildingIdByUserId(userId);
         if (buildingId.isPresent()) {
             Building building = buildingRepository.findById(buildingId.get())
                     .orElseThrow(() -> new BadRequestException("Building not found with ID: " + buildingId.get()));
             model.setBuilding(building);

         }
         model.setMaterialType(scheduleDTO.getMaterialType());

         Resident resident = residentRepository.findByUser_UserId(userId)
                 .orElseThrow(() -> new BadRequestException("Resident not found with ID: " + userId));
         model.setResidentId(resident);

         return scheduleRepository.save(model);
     }
     catch (Exception e){
            throw new BadRequestException(e.getMessage());
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found with id: " + userId));
        Collector collector = user.getCollector();

        return scheduleRepository.findByCollectorAndStatusIn(collector,
                List.of(Schedule.scheduleStatus.ACCEPTED));
    }




    @Override
    public Schedule getScheduleById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
        return schedule;
    }

    @Override
    public Boolean cancelScheduleById(CancelScheduleRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch" + dto.getScheduleId()));
        if (schedule.getCollector() != null &&  schedule.getCollector().getUser().getUsername().equals(userName)) {
            // nếu là collector thì remove collector đó ra khỏi schedule và set status lại là pending
            schedule.setCollector(null);
            schedule.setStatus(Schedule.scheduleStatus.PENDING);
            schedule.setCollectorNote(dto.getReason());
            scheduleRepository.save(schedule);
            return true;
        }
        else if (schedule.getResidentId().getUser().getUsername().equals(userName)) {
            schedule.setStatus(Schedule.scheduleStatus.CANCELED);
            schedule.setResidentNote(dto.getReason());
            scheduleRepository.save(schedule);
            return true;
        }
        else{
            throw new BadRequestException("Ban không có quyền hủy lịch này");
        }
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
