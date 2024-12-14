package be.api.services;

import be.api.model.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBuildingService {
    Building createBuilding(Building building);
    Building updateBuildingById(Integer id, Building building);
    List<Building> getAllBuildings();
}
