package be.api.services;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.model.Resident;
import be.api.model.UserVoucher;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IResidentServices {
    Boolean changePointToVoucher(int voucherId);
    List<UserVoucher> getListVoucherByResidentId();
}
