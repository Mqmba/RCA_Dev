package be.api.services.impl;

import be.api.exception.BadRequestException;
import be.api.model.Collector;
import be.api.model.Payment_History;
import be.api.model.RecyclingDepot;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IPaymentHistoryRepository;
import be.api.repository.IRecyclingDepotRepository;
import be.api.repository.IUserRepository;
import be.api.services.IPaymentHistoryServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentHistoryServices implements IPaymentHistoryServices
{

    private final IPaymentHistoryRepository paymentHistoryRepository;
    private final IUserRepository userRepository;
    private final ICollectorRepository collectorRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;

    @Override
    public Boolean createPaymentHistory(String orderCode, int paymentStatus, int NumberPoint) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw new BadRequestException("Không tìm thấy user");
        }

        Payment_History paymentHistory = new Payment_History();

        paymentHistory.setOrderCode(orderCode);
        paymentHistory.setPaymentStatus(paymentStatus);
        paymentHistory.setNumberPoint(NumberPoint);
        paymentHistory.setUser(user);

        paymentHistoryRepository.save(paymentHistory);

        return true;
    }

    @Override
    public Boolean updateSuccessPayment(String orderCode) {
        Payment_History paymentHistory = paymentHistoryRepository.findByOrderCode(orderCode);
        paymentHistory.setPaymentStatus(2);
        paymentHistoryRepository.save(paymentHistory);
        User user = paymentHistory.getUser();
        RecyclingDepot recyclingDepot = user.getRecyclingDepot();
        recyclingDepot.setBalance(recyclingDepot.getBalance() + paymentHistory.getNumberPoint());
        recyclingDepotRepository.save(recyclingDepot);

        return true;
    }

    @Override
    public Boolean updateSuccessPaymentCollector(String orderCode) {
        Payment_History paymentHistory = paymentHistoryRepository.findByOrderCode(orderCode);
        paymentHistory.setPaymentStatus(2);
        paymentHistoryRepository.save(paymentHistory);
        User user = paymentHistory.getUser();
        Collector collector = user.getCollector();
        collector.setNumberPoint(collector.getNumberPoint() + paymentHistory.getNumberPoint());
        collectorRepository.save(collector);
        return true;
    }

    @Override
    public Boolean changePointFromDepotToCollector(Long point, int collectorId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw new BadRequestException("Không tìm thấy user");
        }
        RecyclingDepot recyclingDepot = user.getRecyclingDepot();
        if(recyclingDepot.getBalance() < point){
            throw new BadRequestException("Người dùng không đủ điểm để đổi");
        }
        recyclingDepot.setBalance(recyclingDepot.getBalance() - point);
        recyclingDepotRepository.save(recyclingDepot);
        Collector collector = collectorRepository.findById(collectorId).get();
        collector.setNumberPoint(collector.getNumberPoint() + point);
        collectorRepository.save(collector);
        return true;
    }


    @Override
    public Payment_History getPaymentHistoryByOrderCode(String orderCode) {
        return paymentHistoryRepository.findByOrderCode(orderCode);
    }

    @Override
    public List<Payment_History> getPaymentHistoryByUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        return paymentHistoryRepository.findByUser(user);

    }


}
