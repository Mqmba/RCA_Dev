package be.api.services.impl;

import be.api.dto.request.VoucherRequestDTO;
import be.api.model.Voucher;
import be.api.repository.IVoucherRepository;
import be.api.services.IVoucherServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherServices  implements IVoucherServices {

    private final IVoucherRepository voucherRepository;
    private final ModelMapper modelMapper;


    @Override
    public Boolean createVoucher(VoucherRequestDTO dto) {
        Voucher voucher = modelMapper.map(dto, Voucher.class);
        voucherRepository.save(voucher);
        log.info("Voucher added successfully");
        return true;
    }

    @Override
    public List<Voucher> getListVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher getVoucherById(int id) {
        return voucherRepository.findById(id).orElse(null);
    }
}
