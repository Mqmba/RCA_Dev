package be.api.services.impl;

import be.api.dto.request.ResidentRegistrationDTO;
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


    @Override
    public Boolean changePointToVoucher(int voucherId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        UserVoucher existingUserVoucher = userVoucherRepository.findByUserAndVoucher(user, voucher);
        if(existingUserVoucher != null){
            throw new ResourceNotFoundException("You already have this voucher");
        }

        if(voucher == null){
            return false;
        }
        Resident resident = user.getResident();
        if(resident.getRewardPoints() < voucher.getPointToRedeem()){
            throw new IllegalArgumentException("Not enough points to redeem this voucher");
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
}