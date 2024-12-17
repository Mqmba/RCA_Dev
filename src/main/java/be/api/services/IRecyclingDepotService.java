package be.api.services;

import be.api.dto.request.CreateDepotRequestDTO;
import be.api.model.RecyclingDepot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRecyclingDepotService {
    Page<RecyclingDepot> getRecyclingDepots(Pageable pageable);
    List<RecyclingDepot> getActiveRecyclingDepots();
    RecyclingDepot createRecyclingDepot(CreateDepotRequestDTO dto);
    RecyclingDepot changeIsWorkingStatus(int id, boolean isWorking);
    RecyclingDepot updateWorkingDate(int id);

    List<RecyclingDepot> getListRecyclingDepots();
}
