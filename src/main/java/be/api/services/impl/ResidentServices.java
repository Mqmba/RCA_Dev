package be.api.services.impl;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.dto.response.AnalyzeMaterial;
import be.api.exception.BadRequestException;
import be.api.exception.ResourceNotFoundException;
import be.api.model.*;
import be.api.repository.*;
import be.api.services.IResidentServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResidentServices implements IResidentServices {

    private static final Logger logger = LoggerFactory.getLogger(ResidentServices.class);
    private final IUserVoucherRepository userVoucherRepository;
    private final IResidentRepository residentRepository;
    private final IUserRepository userRepository;
    private final IVoucherRepository voucherRepository;
    private final ICRPaymentDetailRepository crPaymentDetailRepository;


    @Override
    public Boolean changePointToVoucher(int voucherId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        UserVoucher existingUserVoucher = userVoucherRepository.findByUserAndVoucher(user, voucher);
        if(existingUserVoucher != null){
            throw new BadRequestException("Bạn đã có voucher này rồi");
        }

        if(voucher == null){
            return false;
        }
        Resident resident = user.getResident();
        if(resident.getRewardPoints() < voucher.getPointToRedeem()){
            throw new BadRequestException("Bạn không đủ điểm để đổi voucher này");
        }
        resident.setRewardPoints(resident.getRewardPoints() - voucher.getPointToRedeem());
        residentRepository.save(resident);
        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setVoucher(voucher);
        userVoucher.setUser(user);
        userVoucher.setUsed(false);
        userVoucherRepository.save(userVoucher);
        return true;
    }

    @Override
    public List<UserVoucher> getListVoucherByResidentId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        return userVoucherRepository.findByUser(user);
    }

    @Override
    public AnalyzeMaterial analyzeMaterialByResidentId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new ResourceNotFoundException("Không tìm thấy user");
        }

        Resident resident = user.getResident();

        List<Object[]> results = residentRepository.findAnalyzeMaterialByResidentId(resident.getResidentId());
        List<AnalyzeMaterial.AnalyzeItem> analyzeItems = new ArrayList<>();
        for (Object[] row : results) {
            String materialTypeName = (String) row[0];
            double totalWeight = ((Number) row[1]).doubleValue();
            analyzeItems.add(new AnalyzeMaterial.AnalyzeItem(materialTypeName, totalWeight));
        }

        List<Object[]> rankingResults = residentRepository.findRankingByResidentId(resident.getResidentId());
        if (rankingResults.isEmpty()) {
            throw new ResourceNotFoundException("Không có dữ liệu ranking!");
        }

        Object[] row = rankingResults.get(0);
        // [0] = ResidentId, [1] = TotalWeight, [2] = Ranking
        long ranking = ((Number) row[2]).longValue();

        AnalyzeMaterial analyzeMaterial = new AnalyzeMaterial();
        analyzeMaterial.setDataAnalyze(analyzeItems);
        analyzeMaterial.setRanking(ranking);

        return analyzeMaterial;
    }


}