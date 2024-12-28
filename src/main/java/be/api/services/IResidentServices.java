package be.api.services;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.dto.response.AnalyzeMaterial;
import be.api.model.Resident;
import be.api.model.UserVoucher;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IResidentServices {
    Boolean changePointToVoucher(int voucherId);
    List<UserVoucher> getListVoucherByResidentId();

    AnalyzeMaterial  analyzeMaterialByResidentId();

}
