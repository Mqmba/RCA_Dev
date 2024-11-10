package be.api.services.impl;

import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.response.CDPaymentResponseDTO;
import be.api.model.Collector;
import be.api.model.CollectorDepotPayment;
import be.api.model.RecyclingDepot;
import be.api.repository.ICollectorRepository;
import be.api.repository.IRecyclingDepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CDPaymentService {

    @Autowired
    private ICollectorRepository collectorRepository;

    @Autowired
    private IRecyclingDepotRepository recyclingDepotRepository;

//    public CDPaymentResponseDTO processPayment(CDPaymentRequestDTO cdPaymentRequest) {
//        Collector collector = collectorRepository.findById(cdPaymentRequest.getCollectorId())
//                .orElseThrow(() -> new RuntimeException("Collector not found"));
//        RecyclingDepot depot = recyclingDepotRepository.findById(cdPaymentRequest.getDepotId())
//                .orElseThrow(() -> new RuntimeException("Recycling Depot not found"));
//
//        // Mô phỏng transactionId và trả về kết quả
//        CollectorDepotPayment payment = new CollectorDepotPayment();
//        return new CDPaymentResponseDTO(payment.getCrPaymentId(), cdPaymentRequest.getAmount(), cdPaymentRequest.getPaymentMethod(), LocalDateTime.now(), "Thanh toán thành công");
//    }
}
