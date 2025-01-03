package be.api.services.impl;

import be.api.controller.RecyclingDepotController;
import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CDPaymentResponse;
import be.api.dto.response.CRPaymentResponse;
import be.api.dto.response.ResponseData;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.ICDPaymentServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CDPaymentServices implements ICDPaymentServices {

    private final ICDPaymentRepository cdPaymentRepository;
    private final ICollectorRepository collectorRepository;
    private final IMaterialRepository materialRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final ICDPaymentDetailRepository crPaymentDetailRepository;
    private final IUserRepository userRepository;
    private final IDepotMaterialRepository depotMaterialRepository;

    @Override
    public Integer createCDPayment(CDPaymentRequestDTO dto) {
        CollectorDepotPayment payment = new CollectorDepotPayment();

        // doan nay phia client truyen collectorId nhung lai la user id :D
        Optional<User> userByUserId = userRepository.findById(dto.getCollectorId());
        Collector collector = userByUserId.get().getCollector();

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);


        payment.setMaterialType(dto.getMaterialType());
        payment.setCollector(collector);
        payment.setRecyclingDepot(user.getRecyclingDepot());

//        payment.setAmount(calculateAmountPoint(dto.getMaterials()));
        double amountFromDepot = 0;
        for (CRPaymentRequestDTO.MaterialDTO material : dto.getMaterials()) {
            double priceFromDepot = depotMaterialRepository
                    .findByMaterialIdAndRecyclingDepotId(material.getMaterialId(), user.getRecyclingDepot().getId())
                    .getPrice();
            amountFromDepot += priceFromDepot * material.getQuantity();
        }
        payment.setAmount(amountFromDepot);


        CollectorDepotPayment savedPayment = cdPaymentRepository.save(payment);
        Integer cdPaymentId = savedPayment.getCdPaymentId();

        // 2. Lưu CDPayment_Detail cho từng vật liệu
        dto.getMaterials().forEach(material -> {
            CDPayment_Detail detail = new CDPayment_Detail();
            detail.setCdPaymentId(cdPaymentId);
            Material model = materialRepository.findById(material.getMaterialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material not found with ID: " + material.getMaterialId()));
            detail.setMaterial(model);
            detail.setQuantity(material.getQuantity());
            crPaymentDetailRepository.save(detail);
        });


        return cdPaymentId;
    }

    @Override
    public Boolean updateSuccessCDPayment(Integer paymentId) {
        try {

            CollectorDepotPayment existingPayment = cdPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId));


            existingPayment.setStatus(CollectorDepotPayment.CollectorDepotPaymentStatus.SUCCESS);

            RecyclingDepot recyclingDepot = existingPayment.getRecyclingDepot();

            Collector collector = existingPayment.getCollector();

            collector.setNumberPoint(collector.getNumberPoint() + existingPayment.getAmount());
            recyclingDepot.setBalance(recyclingDepot.getBalance() - existingPayment.getAmount());

            collectorRepository.save(collector);
            recyclingDepotRepository.save(recyclingDepot);
            cdPaymentRepository.save(existingPayment);


            return true;
        } catch (Exception e) {
            log.error("Error while updating payment with ID: " + paymentId, e);
            return false;
        }
    }

    @Override
    public CRPaymentResponse getCDPaymentById(Integer paymentId) {
        return null;
    }

    @Override
    public List<CDPaymentResponse> getListPayment() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        RecyclingDepot recyclingDepot = user.getRecyclingDepot();
         List<CollectorDepotPayment> payments = cdPaymentRepository.findByRecyclingDepot(recyclingDepot);
         List<CDPaymentResponse> response = new ArrayList<>();
         for (CollectorDepotPayment payment : payments) {
                CDPaymentResponse cdPaymentResponse = new CDPaymentResponse();
                cdPaymentResponse.setCollectorDepotPayment(payment);
                List<CDPayment_Detail> listDetail = crPaymentDetailRepository.findByCdPaymentId(payment.getCdPaymentId());
                cdPaymentResponse.setCdPaymentDetail(listDetail);
                response.add(cdPaymentResponse);
         }
         return response;
    }

    private double calculateAmountPoint(List<CRPaymentRequestDTO.MaterialDTO> materials) {
        return materials.stream()
                .mapToDouble(material -> {
                    double basePrice = getPriceFromMaterial(material.getMaterialId());
                    double increasedPrice = basePrice * 1.10; // Tăng giá thêm 10%
                    double total = material.getQuantity() * increasedPrice;
                    return total; // Tổng giá trị làm tròn thành số nguyên
                })
                .sum();
    }

    private Double getPriceFromMaterial(Integer materialId) {
        Optional<Material> material = materialRepository.findById(materialId);
        return material.map(Material::getPrice).orElse(Double.valueOf(0)); // Trả về giá hoặc 0 nếu không tìm thấy
    }
}
