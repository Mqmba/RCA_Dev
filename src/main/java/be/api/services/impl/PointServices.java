package be.api.services.impl;

import be.api.dto.request.PointRequestDTO;
import be.api.exception.BadRequestException;
import be.api.exception.ResourceNotFoundException;
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
            throw new BadRequestException(e.getMessage());
        }
        return 0;
    }

    @Override
    @Transactional
    public String updatePointByUser(PointRequestDTO pointRequest) {
        if (pointRequest == null) {
            throw new BadRequestException("Không được để trống thông tin");
        }

        try {
            int senderId = pointRequest.getSenderId();
            int receiverId = pointRequest.getReceiverId();
            int amount = pointRequest.getAmount();
            Optional<Collector> collector = collectorRepository.findById(senderId);
            Optional<Resident> resident = residentRepository.findById(receiverId);
            if (collector.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy collector");
            if (resident.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy resident");
            if(collector.get().getNumberPoint() - amount > 0){
                collectorRepository.updateByCollectorId(senderId, amount);
                residentRepository.updateResidentById(receiverId, amount);
                return "Resident " + resident.get().getUser().getUsername() + " received " + amount + " from " + collector.get().getUser().getUsername();
            }
            else{
                throw new BadRequestException("Khong đủ điểm để chuyển");
            }
        } catch (EntityNotFoundException e) {
            throw new BadRequestException("Entity not found");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid argument provided");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }
}