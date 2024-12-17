package be.api.services;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.model.Resident;
import org.springframework.data.domain.Page;

public interface IResidentServices {
    Resident createResident(ResidentRegistrationDTO residentData);
    Page<Resident> getPaginateResident(int page, int size);
}
