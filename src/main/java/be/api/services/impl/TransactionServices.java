package be.api.services.impl;

import be.api.exception.BadRequestException;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.ITransactionServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
public class TransactionServices implements ITransactionServices {
    private final IUserRepository userRepository;
    private final ICollectorRepository collectorRepository;
    private final IResidentRepository residentRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final ITransactionHistory transactionHistory;

    @Override
    @Transactional
    public Boolean transferPoint(int receiverId, double numberPoint) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUsername(userName);

        if (sender == null) {
            throw new EntityNotFoundException("Người gửi không tồn tại");
        }

        switch (sender.getRole()) {
            case ROLE_COLLECTOR -> handleCollectorTransfer(sender, receiverId, numberPoint);
            case ROLE_RESIDENT -> handleResidentTransfer(sender, receiverId, numberPoint);
            case ROLE_RECYCLING_DEPOT -> handleRecyclingDepotTransfer(sender, receiverId, numberPoint);
            default -> {
                log.error("Vai trò không hợp lệ: {}", sender.getRole());
                throw new IllegalArgumentException("Vai trò không hợp lệ");
            }
        }

        return true;
    }

    @Override
    public List<TransactionHistory> getListTransactionHistoryByUserId(int userId) {
        return transactionHistory.findByUser_UserId(userId);
    }

    @Override
    public List<TransactionHistory> getListTransactionHistoryByToken() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        return transactionHistory.findByUser_UserId(user.getUserId());
    }

    private void handleCollectorTransfer(User sender, int receiverId, double numberPoint) {
        Collector senderCollector = sender.getCollector();

        if (senderCollector.getNumberPoint() < numberPoint) {
            throw new BadRequestException("Số điểm không đủ để chuyển");
        }

        // Trừ điểm từ người gửi
        senderCollector.setNumberPoint(senderCollector.getNumberPoint() - numberPoint);
        collectorRepository.save(senderCollector);

        // Chuyển điểm cho người nhận
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người nhận"));
        switch (receiver.getRole()) {
            case ROLE_COLLECTOR -> transferToCollector(sender, receiver, numberPoint);
            case ROLE_RESIDENT -> transferToResident(sender, receiver, numberPoint);
            case ROLE_RECYCLING_DEPOT -> transferToRecyclingDepot(sender,receiver, numberPoint);
            default -> throw new IllegalArgumentException("Vai trò người nhận không hợp lệ");
        }
    }

    private void handleResidentTransfer(User sender, int receiverId, double numberPoint) {
        Resident senderResident = sender.getResident();

        if (senderResident.getRewardPoints() < numberPoint) {
            throw new BadRequestException("Số điểm không đủ để chuyển");
        }

        // Trừ điểm từ người gửi
        senderResident.setRewardPoints(senderResident.getRewardPoints() - numberPoint);
        residentRepository.save(senderResident);

        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người nhận"));
        switch (receiver.getRole()) {
            case ROLE_COLLECTOR -> transferToCollector(sender,receiver, numberPoint);
            case ROLE_RESIDENT -> transferToResident(sender,receiver, numberPoint);
            case ROLE_RECYCLING_DEPOT -> transferToRecyclingDepot(sender,receiver, numberPoint);
            default -> throw new IllegalArgumentException("Vai trò người nhận không hợp lệ");
        }
    }

    private void handleRecyclingDepotTransfer(User sender, int receiverId, double numberPoint) {
        RecyclingDepot senderDepot = sender.getRecyclingDepot();

        if (senderDepot.getBalance() < numberPoint) {
            throw new BadRequestException("Số điểm không đủ để chuyển");
        }

        senderDepot.setBalance(senderDepot.getBalance() - numberPoint);
        recyclingDepotRepository.save(senderDepot);

        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người nhận"));
        switch (receiver.getRole()) {
            case ROLE_COLLECTOR -> transferToCollector(sender,receiver, numberPoint);
            case ROLE_RESIDENT -> transferToResident(sender,receiver, numberPoint);
            case ROLE_RECYCLING_DEPOT -> transferToRecyclingDepot(sender,receiver, numberPoint);
            default -> throw new IllegalArgumentException("Vai trò người nhận không hợp lệ");
        }

    }

    private void transferToCollector(User senderUser,User receiver, double numberPoint) {
        Collector receiverCollector = receiver.getCollector();
        receiverCollector.setNumberPoint(receiverCollector.getNumberPoint() + numberPoint);
        collectorRepository.save(receiverCollector);
        TransactionHistory transaction = new TransactionHistory();
        String orderCode = generateOrderCode();
        transaction.setOrderCode(orderCode);
        transaction.setUser(senderUser);
        transaction.setReceiverId(receiver);
        transaction.setNumberPoint(numberPoint);
        transaction.setStatus(TransactionHistory.TransactionType.SUCCESS);
        transactionHistory.save(transaction);

    }

    private void transferToResident(User senderUser, User receiver, double numberPoint) {
        Resident receiverResident = receiver.getResident();
        receiverResident.setRewardPoints(receiverResident.getRewardPoints() + numberPoint);
        residentRepository.save(receiverResident);
        TransactionHistory transaction = new TransactionHistory();
        String orderCode = generateOrderCode();
        transaction.setOrderCode(orderCode);
        transaction.setUser(senderUser);
        transaction.setReceiverId(receiver);
        transaction.setNumberPoint(numberPoint);
        transaction.setStatus(TransactionHistory.TransactionType.SUCCESS);
        transactionHistory.save(transaction);
    }

    private void transferToRecyclingDepot(User senderUser, User receiver, double numberPoint) {
        RecyclingDepot receiverDepot = receiver.getRecyclingDepot();
        receiverDepot.setBalance(receiverDepot.getBalance() + numberPoint);
        recyclingDepotRepository.save(receiverDepot);
        TransactionHistory transaction = new TransactionHistory();
        String orderCode = generateOrderCode();
        transaction.setOrderCode(orderCode);
        transaction.setUser(senderUser);
        transaction.setReceiverId(receiver);
        transaction.setNumberPoint(numberPoint);
        transaction.setStatus(TransactionHistory.TransactionType.SUCCESS);
        transactionHistory.save(transaction);
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