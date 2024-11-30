package be.api.services.impl;

import be.api.dto.response.AdminActivityResponseDTO;
import be.api.dto.response.AdminDashboardResponseDTO;
import be.api.dto.response.AdminTransactionResponseDTO;
import be.api.model.Collector;
import be.api.model.Resident;
import be.api.model.Schedule;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IResidentRepository;
import be.api.repository.IScheduleRepository;
import be.api.repository.IUserRepository;
import be.api.services.IAdminServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServices implements IAdminServices  {

    private final IUserRepository userRepository;
    private final IScheduleRepository scheduleRepository;
    private final IResidentRepository residentRepository;
    private final ICollectorRepository collectorRepository;

    @Override
    public AdminDashboardResponseDTO getAdminDashBoard() {
        try{
            AdminDashboardResponseDTO adminDashboardResponseDTO = new AdminDashboardResponseDTO();

            long totalDepotAccount = userRepository.getTotalAccountByRole(User.UserRole.ROLE_RECYCLING_DEPOT);
            long totalCollectorAccount = userRepository.getTotalAccountByRole(User.UserRole.ROLE_COLLECTOR);
            long totalResidentAccount = userRepository.getTotalAccountByRole(User.UserRole.ROLE_RESIDENT);
            long totalTransaction = scheduleRepository.findByNumberTransaction(
                    Schedule.scheduleStatus.SUCCESS
            );

            List<Resident> top5Resident = residentRepository.findTop5ByOrderByRewardPointsDesc();

            adminDashboardResponseDTO.setNumberAccountDepot(totalDepotAccount);
            adminDashboardResponseDTO.setNumberAccountCollector(totalCollectorAccount);
            adminDashboardResponseDTO.setNumberAccountResident(totalResidentAccount);
            adminDashboardResponseDTO.setNumberTransaction(totalTransaction);
            adminDashboardResponseDTO.setTopListResident(top5Resident);

            return adminDashboardResponseDTO;
        }
        catch (Exception e){
            log.error("Error in AdminServices.getAdminDashBoard: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public List<Collector> getAllCollector() {
        return collectorRepository.findAll();
    }

    @Override
    public List<User> getAllDepot() {
        return userRepository.findByRole(User.UserRole.ROLE_RECYCLING_DEPOT);
    }

    @Override
    public AdminActivityResponseDTO getAdminActivity() {
        AdminActivityResponseDTO adminActivityResponseDTO = new AdminActivityResponseDTO();
        long totalTransaction = scheduleRepository.findByNumberTransaction(
                    Schedule.scheduleStatus.SUCCESS
            );
        long totalResident = userRepository.getTotalAccountByRole(User.UserRole.ROLE_RESIDENT);
        List<Resident> residents = residentRepository.findAll();
        double avgTransactionPoint = 0;
        for (Resident resident: residents){
            avgTransactionPoint += resident.getRewardPoints();
        }
        avgTransactionPoint = avgTransactionPoint / totalResident;

        adminActivityResponseDTO.setNumberTransaction(totalTransaction);
        adminActivityResponseDTO.setAvgTransactionPoint(avgTransactionPoint);
        adminActivityResponseDTO.setResidentList(residents);
        adminActivityResponseDTO.setNumberAccountResident(totalResident);

        return adminActivityResponseDTO;
    }

    public AdminTransactionResponseDTO getAdminTransaction() {
        AdminTransactionResponseDTO adminTransactionResponseDTO = new AdminTransactionResponseDTO();
        long totalTransaction = scheduleRepository.findAll().size();
        long totalTransactionOngoing = scheduleRepository.findByAccepteAndOnGoing(Schedule.scheduleStatus.ACCEPTED);
        long totalTransactionPending = scheduleRepository.findByStatus(Schedule.scheduleStatus.PENDING).size();
        long totalTransactionFinished = scheduleRepository.findByStatus(Schedule.scheduleStatus.SUCCESS).size();
        List<Schedule> top5ScheduleByCreatedAt = scheduleRepository.findTop5ByOrderByCreatedAtDesc();


        adminTransactionResponseDTO.setNumberTransaction(totalTransaction);
        adminTransactionResponseDTO.setNumberTransactionGoing(totalTransactionOngoing);
        adminTransactionResponseDTO.setNumberTransactionPending(totalTransactionPending);
        adminTransactionResponseDTO.setNumberTransactionSuccess(totalTransactionFinished);
        adminTransactionResponseDTO.setTop5ScheduleByCreatedAt(top5ScheduleByCreatedAt);


        return adminTransactionResponseDTO;
    }

}

