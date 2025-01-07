package be.api.services.impl;

import be.api.dto.request.CRPaymentRequestDTO;
import be.api.dto.response.CRPaymentResponse;
import be.api.exception.BadRequestException;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.ICRPaymentServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CRPaymentServices implements ICRPaymentServices {

    private final ICRPaymentRepository crPaymentRepository;
    private final ICRPaymentDetailRepository crPaymentDetailRepository;
    private final IScheduleRepository scheduleRepository;
    private final ICollectorRepository collectorRepository;
    private final IResidentRepository residentRepository;
    private final IMaterialRepository materialRepository;
    private final IUserRepository userRepository;

    @Override
    public Integer createCRPayment(CRPaymentRequestDTO dto) {
        // 1. Lưu CollectorResident_Payment
        CollectorResidentPayment existingPayment = crPaymentRepository.findByScheduleId(dto.getScheduleId());
        if(existingPayment != null){
            existingPayment.setAmountPoint(calculateAmountPoint(dto.getMaterials()));
            CollectorResidentPayment updatedPayment = crPaymentRepository.save(existingPayment);
            List<CRPayment_Detail> existingDetails = crPaymentDetailRepository.findByCrPaymentId(updatedPayment.getCrPaymentId());
            crPaymentDetailRepository.deleteAll(existingDetails);
            dto.getMaterials().forEach(material -> {
                CRPayment_Detail detail = new CRPayment_Detail();
                detail.setCrPaymentId(updatedPayment.getCrPaymentId());
                Material model = materialRepository.findById(material.getMaterialId())
                        .orElseThrow(() -> new BadRequestException("Không tìm thấy material " + material.getMaterialId()));
                detail.setMaterial(model);
                detail.setQuantity(material.getQuantity());
                crPaymentDetailRepository.save(detail);
            });

            // Trả về ID của CollectorResidentPayment đã cập nhật
            return updatedPayment.getCrPaymentId();

        }
        else{
            CollectorResidentPayment payment = new CollectorResidentPayment();
            Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("Schedule not found with ID: " + dto.getScheduleId()));
            payment.setSchedule(schedule);
//        payment.setAmountPoint(2222);
            payment.setAmountPoint(calculateAmountPoint(dto.getMaterials()));
            payment.setStatus(1);

            CollectorResidentPayment savedPayment = crPaymentRepository.save(payment);
            Integer crPaymentId = savedPayment.getCrPaymentId();

            // 2. Lưu CRPayment_Detail cho từng vật liệu
            dto.getMaterials().forEach(material -> {
                CRPayment_Detail detail = new CRPayment_Detail();
                detail.setCrPaymentId(crPaymentId);
                Material model = materialRepository.findById(material.getMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("Material not found with ID: " + material.getMaterialId()));
                detail.setMaterial(model);
                detail.setQuantity(material.getQuantity());
                crPaymentDetailRepository.save(detail);
            });


            // Trả về ID của CollectorResident_Payment vừa tạo
            return crPaymentId;
        }


    }

    @Override
    public Boolean updateSuccessCRPayment(Integer paymentId) {
        try{
            CollectorResidentPayment existingPayment = crPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new BadRequestException("Thông tin thanh toán không  tìm thấy: " + paymentId));

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(userName);

            if(user == null){
                throw new BadRequestException("Không tìm thấy thông tin người dùng");
            }

            if(existingPayment.getSchedule().getCollector().getUser().getUserId() != user.getUserId()){
                throw new BadRequestException("Chỉ có người thu gom của đơn này mới được xác nhận");
            }

            if(existingPayment.getStatus() == 2){
                throw new BadRequestException("Đơn này đã xác nhận thanh toán thành công");
            }

            existingPayment.setStatus(2);
            Schedule schedule = existingPayment.getSchedule();
            schedule.setStatus(Schedule.scheduleStatus.SUCCESS);

            Resident resident = schedule.getResidentId();
            resident.setRewardPoints(resident.getRewardPoints() + existingPayment.getAmountPoint());

            Collector collector = schedule.getCollector();
            collector.setNumberPoint(collector.getNumberPoint() - existingPayment.getAmountPoint());

            scheduleRepository.save(schedule);
            residentRepository.save(resident);
            collectorRepository.save(collector);
            crPaymentRepository.save(existingPayment);
            return true;
        }
        catch (Exception e){
            log.error("Error while updating CRPayment: {}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public CRPaymentResponse getCRPaymentById(Integer paymentId) {

        CollectorResidentPayment payment =  crPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId));
        List<CRPayment_Detail> detail = crPaymentDetailRepository.findByCrPaymentId(paymentId);

        CRPaymentResponse response = new CRPaymentResponse();
        response.setPayment(payment);
        response.setPaymentDetails(detail);

        return response;
    }

    @Override
    public CRPaymentResponse findByScheduleId(Integer scheduleId) {
        CollectorResidentPayment payment = crPaymentRepository.findByScheduleId(scheduleId);
        List<CRPayment_Detail> detail = crPaymentDetailRepository.findByCrPaymentId(payment.getCrPaymentId());

        CRPaymentResponse response = new CRPaymentResponse();
        response.setPayment(payment);
        response.setPaymentDetails(detail);

        return response;
    }


    private Integer calculateAmountPoint(List<CRPaymentRequestDTO.MaterialDTO> materials) {
        return materials.stream()
                .mapToInt(material -> (int) (material.getQuantity() * getPriceFromMaterial(material.getMaterialId())))
                .sum();
    }

    private Double getPriceFromMaterial(Integer materialId) {
        Optional<Material> material = materialRepository.findById(materialId);
        return material.map(Material::getPrice).orElse(Double.valueOf(0)); // Trả về giá hoặc 0 nếu không tìm thấy
    }
}
