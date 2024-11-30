package be.api.services.impl;

import be.api.dto.request.CDPaymentRequestDTO;
import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CRPaymentResponse;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.ICDPaymentServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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


    @Override
    public Integer createCDPayment(CDPaymentRequestDTO dto) {
        CollectorDepotPayment payment = new CollectorDepotPayment();
        Collector collector = collectorRepository.findById(dto.getCollectorId())
                .orElseThrow(() -> new IllegalArgumentException("Collector not found with ID: " + dto.getCollectorId()));


        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);

        payment.setMaterialType(dto.getMaterialType());
        payment.setCollector(collector);
        payment.setRecyclingDepot(user.getRecyclingDepot());

        payment.setAmount(calculateAmountPoint(dto.getMaterials()));


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
