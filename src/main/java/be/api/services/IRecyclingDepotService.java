package be.api.services;

import be.api.model.RecyclingDepot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecyclingDepotService {
    Page<RecyclingDepot> getRecyclingDepots(Pageable pageable);
    Page<RecyclingDepot> getActiveRecyclingDepots(Pageable pageable);
    RecyclingDepot createRecyclingDepot(RecyclingDepot recyclingDepot);
    RecyclingDepot changeIsWorkingStatus(int id, boolean isWorking);
    RecyclingDepot updateWorkingDate(int id);
}
