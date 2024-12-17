package be.api.services;

import be.api.model.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBuildingService {
    Building createBuilding(Building building);
    Building updateBuildingById(Integer id, Building building);
    Page<Building> getAllBuildings(Pageable pageable);
}
