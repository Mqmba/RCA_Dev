package be.api.services.impl;

import be.api.dto.request.DrawMoneyRequestDTO;
import be.api.exception.BadRequestException;
import be.api.model.DrawMoneyHistory;
import be.api.model.User;
import be.api.repository.*;
import be.api.services.IDrawMoneyServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrawMoneyServices implements IDrawMoneyServices {

    private final IDrawMoneyHistoryRepository drawMoneyHistoryRepository;
    private final IUserRepository userRepository;
    private final IResidentRepository residentRepository;
    private final ICollectorRepository collectorRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;


    @Override
    public DrawMoneyHistory createDrawMoneyRequest(DrawMoneyRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);

        if(user == null){
            throw new BadRequestException("Không tìm thấy user");
        }

        if(user.getRole() == User.UserRole.ROLE_COLLECTOR){
            if(user.getCollector().getNumberPoint() <= dto.getNumberPoint()){
                throw new BadRequestException("Số điểm không đủ");
            }
            else{
                user.getCollector().setNumberPoint(user.getCollector().getNumberPoint() - dto.getNumberPoint());
                collectorRepository.save(user.getCollector());
            }
        }
        else if(user.getRole() == User.UserRole.ROLE_RESIDENT){
            if(user.getResident().getRewardPoints() <= dto.getNumberPoint()){
                throw new BadRequestException("Số điểm không đủ");
            }
            else{
                user.getResident().setRewardPoints(user.getResident().getRewardPoints() - dto.getNumberPoint());
                residentRepository.save(user.getResident());
            }
        }
        else if(user.getRole() == User.UserRole.ROLE_RECYCLING_DEPOT){
            if(user.getRecyclingDepot().getBalance() <= dto.getNumberPoint()){
                throw new BadRequestException("Số điểm không đủ");
            }
            else{
                user.getRecyclingDepot().setBalance(user.getRecyclingDepot().getBalance() - dto.getNumberPoint());
                recyclingDepotRepository.save(user.getRecyclingDepot());
            }
        }

        DrawMoneyHistory drawMoneyHistory = new DrawMoneyHistory();


        drawMoneyHistory.setNumberPoint(dto.getNumberPoint());
        drawMoneyHistory.setOrderCode(generateOrderCode());
        drawMoneyHistory.setAmount(dto.getNumberPoint() * 1000);
        drawMoneyHistory.setUser(user);
        drawMoneyHistory.setBankName(dto.getBankName());
        drawMoneyHistory.setBankAccountName(dto.getBankAccountName());
        drawMoneyHistory.setBankAccountNumber(dto.getBankAccountNumber());
        drawMoneyHistory.setStatus(DrawMoneyHistory.STATUS.PENDING);
        drawMoneyHistoryRepository.save(drawMoneyHistory);
        return drawMoneyHistory;
}

    @Override
    public DrawMoneyHistory setStatusDrawMoneyRequest(int drawMoneyId, DrawMoneyHistory.STATUS status) {
        DrawMoneyHistory drawMoneyHistory = drawMoneyHistoryRepository.findById(drawMoneyId).orElse(null);
        if (drawMoneyHistory == null) {
            throw new BadRequestException("Không tìm thấy lịch sử rút tiền");
        }
        drawMoneyHistory.setStatus(status);
        drawMoneyHistoryRepository.save(drawMoneyHistory);
        return drawMoneyHistory;
    }

    @Override
    public List<DrawMoneyHistory> getListDrawMoneyRequestByUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        return drawMoneyHistoryRepository.findByUser_UserId(user.getUserId());
    }

    @Override
    public List<DrawMoneyHistory> getAllDrawMoney() {
        return drawMoneyHistoryRepository.findAll();
    }


    public static String generateOrderCode() {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String lastSixDigits = timestamp.substring(timestamp.length() - 6);

        Random random = new Random();
        int randomNum = 100 + random.nextInt(900);

        String orderCodeStr = lastSixDigits + randomNum;

        return orderCodeStr;
    }
}
