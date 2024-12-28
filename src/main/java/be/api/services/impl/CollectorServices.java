package be.api.services.impl;

import be.api.dto.response.ScheduleResponseDTO;
import be.api.exception.ResourceNotFoundException;

import be.api.exception.BadRequestException;
import be.api.model.Collector;
import be.api.model.Resident;
import be.api.model.Schedule;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IResidentRepository;
import be.api.repository.IScheduleRepository;
import be.api.repository.IUserRepository;
import be.api.services.ICollectorServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectorServices implements ICollectorServices {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CollectorServices.class);
    private final ICollectorRepository collectorRepository;
    private final IUserRepository userRepository;
    private final ScheduleService scheduleService;
    private final IScheduleRepository scheduleRepository;
    private final IResidentRepository residentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Collector> getAllCollectors(int page, int size) {
            try {
                Pageable pageable = PageRequest.of(page, size);
                return collectorRepository.findAll(pageable);
            } catch (Exception e) {
                logger.error("Error while retrieving apartments: {}", e.getMessage());
                return Page.empty();
            }
    }

    @Override
    public Collector updateCollector(boolean isWorking,int collectorId) {
        try{
            Collector collector = collectorRepository.findById(collectorId)
                    .orElseThrow(() -> new IllegalArgumentException("Collector not found with ID: " + collectorId));
            collector.setIsWorking(isWorking);
            return collectorRepository.save(collector);
        }catch (Exception e){
            logger.error("Error while updating collector: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public Schedule acceptCollectSchedule(Integer scheduleId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfCurrentHour = now.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfCurrentHour = startOfCurrentHour.plusHours(1);

        // Chuyển đổi LocalDateTime sang Date để so sánh
        Date startDate = java.sql.Timestamp.valueOf(startOfCurrentHour);
        Date endDate = java.sql.Timestamp.valueOf(endOfCurrentHour);

        // Lấy danh sách lịch của user trong khoảng thời gian hiện tại
        List<Schedule> schedulesInCurrentHour = scheduleService.getUserSchedules(user.getUserId())
                .stream()
                .filter(schedule ->
                        schedule.getUpdatedAt().compareTo(startDate) >= 0 &&
                                schedule.getUpdatedAt().compareTo(endDate) < 0
                )
                .collect(Collectors.toList());

        if (schedulesInCurrentHour.size() >= 2) {
            throw new BadRequestException("Bạn chỉ được nhận tối đa 2 lịch mỗi giờ!");
        }
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy lịch"));

        existingSchedule.setStatus(Schedule.scheduleStatus.ACCEPTED);
        existingSchedule.setCollector(user.getCollector());

        return scheduleRepository.save(existingSchedule);
    }


    // danh cho user (get list schedule cua collector)
    @Override
    public List<Schedule> getSchedulesByStatus(Schedule.scheduleStatus status) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(userName);

        if(user.getRole() == User.UserRole.ROLE_COLLECTOR){
            return scheduleRepository.findByCollectorAndStatus(user.getCollector().getCollectorId(),status);
        }
        else if(user.getRole() == User.UserRole.ROLE_RESIDENT){
            return scheduleRepository.findByResidentAndStatus(user.getResident().getResidentId(),status);

        }
        return null;
    }

    @Override
    public List<Schedule> getAllScheduleByUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        User.UserRole role = user.getRole();
        if (role == User.UserRole.ROLE_COLLECTOR) {
            return scheduleRepository.findByCollector(user.getCollector().getCollectorId());
        }
        else if (role == User.UserRole.ROLE_RESIDENT) {
            return  scheduleRepository.findByResidentId(user.getResident().getResidentId());
        }
        return null;
    }

    @Override
    public Boolean changeBalanceToPoint(long point) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw new BadRequestException("Không tìm thấy user");
        }
        Collector collector = user.getCollector();
        Boolean isCanChange = collector.getBalance() >= (point * 1000);
        if (!isCanChange) {
            throw new BadRequestException("Người dùng không đủ điểm để đổi");
        }
        collector.setBalance(collector.getBalance() - (point * 1000));
        collector.setNumberPoint(collector.getNumberPoint() + point);
        collectorRepository.save(collector);
        return true;
    }


    // get all list schedule theo status
    @Override
    public List<Schedule> getListScheduleByStatus(Schedule.scheduleStatus status) {
        List<Schedule> data = scheduleRepository.findByStatus(status);
//        List<ScheduleResponseDTO> result = new ArrayList<>();
//        for (Schedule schedule : data) {
//            result.add(modelMapper.map(schedule, ScheduleResponseDTO.class));
//        }
        return data;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


}
