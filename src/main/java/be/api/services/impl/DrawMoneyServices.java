package be.api.services.impl;

import be.api.dto.request.DrawMoneyRequestDTO;
import be.api.exception.BadRequestException;
import be.api.model.DrawMoneyHistory;
import be.api.model.User;
import be.api.repository.IDrawMoneyHistoryRepository;
import be.api.repository.IUserRepository;
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

    @Override
    public DrawMoneyHistory createDrawMoneyRequest(DrawMoneyRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
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


    public static String generateOrderCode() {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String lastSixDigits = timestamp.substring(timestamp.length() - 6);

        Random random = new Random();
        int randomNum = 100 + random.nextInt(900);

        String orderCodeStr = lastSixDigits + randomNum;

        return orderCodeStr;
    }
}
