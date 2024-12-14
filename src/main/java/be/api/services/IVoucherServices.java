package be.api.services;

import be.api.dto.request.VoucherRequestDTO;
import be.api.model.Voucher;

import java.util.List;

public interface IVoucherServices {
    Boolean createVoucher(VoucherRequestDTO dto);
    List<Voucher> getListVoucher();
    Voucher getVoucherById(int id);
}
