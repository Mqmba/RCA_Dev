package be.api.services.impl;

import be.api.model.Collector;
import be.api.model.Schedule;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IScheduleRepository;
import be.api.repository.IUserRepository;
import be.api.services.ICollectorServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectorServices implements ICollectorServices {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CollectorServices.class);
    private final ICollectorRepository collectorRepository;
    private final IUserRepository userRepository;
    private final ScheduleService scheduleService;
    private final IScheduleRepository scheduleRepository;

    public CollectorServices(ICollectorRepository collectorRepository, IUserRepository userRepository, ScheduleService scheduleService, IScheduleRepository scheduleRepository) {
        this.collectorRepository = collectorRepository;
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }

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
        List<Schedule> schedule = scheduleService.getUserSchedules(user.getUserId());
        if(schedule.size() >= 5){
            throw new IllegalArgumentException("Schedule exceeds limit");
        }
        Schedule updateSchedule = new Schedule();
        updateSchedule.setScheduleId(scheduleId);
        updateSchedule.setStatus(Schedule.scheduleStatus.ACCEPTED);
        updateSchedule.setCollector(user.getCollector());
        return scheduleRepository.save(updateSchedule);
    }

    @Override
    public List<Schedule> getSchedulesByStatus(Schedule.scheduleStatus status) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        return scheduleService.getUserSchedules(user.getUserId(),status);
    }
}
