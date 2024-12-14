package be.api.services.impl;

import be.api.dto.request.PointRequestDTO;
import be.api.model.Collector;
import be.api.model.Resident;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IResidentRepository;
import be.api.repository.IUserRepository;
import be.api.security.JwtTokenUtil;
import be.api.services.IPointServices;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PointServices implements IPointServices {
    private final ICollectorRepository collectorRepository;
    private final IResidentRepository residentRepository;
    private final IUserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public PointServices(ICollectorRepository collectorRepository, IResidentRepository residentRepository, IUserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.collectorRepository = collectorRepository;
        this.residentRepository = residentRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

//    @Override
//    public int getPoints(@Nullable Integer collectorId, @Nullable Integer residentId) {
//        try {
//            if (collectorId != null) {
//                return collectorRepository.findById(collectorId)
//                        .map(Collector::getNumberPoint)
//                        .orElse(0);
//            } else if (residentId != null) {
//                return residentRepository.findById(residentId)
//                        .map(Resident::getRewardPoints)
//                        .orElse(0);
//            } else {
//                return 0;
//            }
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Failed to retrieve points", e);
//        }
//    }

    @Override
    public double getPoints() {
        try{
           String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(userName);
            if(user.getRole() == User.UserRole.ROLE_COLLECTOR) {
                return user.getCollector().getNumberPoint();
            } else if(user.getRole() == User.UserRole.ROLE_RESIDENT) {
                return user.getResident().getRewardPoints();
            }
            else if(user.getRole() == User.UserRole.ROLE_RECYCLING_DEPOT){
                return user.getRecyclingDepot().getBalance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve points", e);
        }
        return 0;
    }

    @Override
    @Transactional
    public String updatePointByUser(PointRequestDTO pointRequest) {
        if (pointRequest == null) {
            throw new IllegalArgumentException("PointRequestDTO cannot be null");
        }

        try {
            int senderId = pointRequest.getSenderId();
            int receiverId = pointRequest.getReceiverId();
            int amount = pointRequest.getAmount();
            Optional<Collector> collector = collectorRepository.findById(senderId);
            Optional<Resident> resident = residentRepository.findById(receiverId);
            if (collector.isEmpty()) throw new IllegalArgumentException("Collector not found");
            if (resident.isEmpty()) throw new IllegalArgumentException("Resident not found");
            if(collector.get().getNumberPoint() - amount > 0){
                collectorRepository.updateByCollectorId(senderId, amount);
                residentRepository.updateResidentById(receiverId, amount);
                return "Resident " + resident.get().getUser().getUsername() + " received " + amount + " from " + collector.get().getUser().getUsername();
            }
            else{
                throw new RuntimeException("Not enough points");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Entity not found", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid argument provided", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}