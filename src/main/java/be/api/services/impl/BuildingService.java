package be.api.services.impl;

import be.api.model.Building;
import be.api.repository.IBuildingRepository;
import be.api.services.IBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BuildingService implements IBuildingService {

    private final IBuildingRepository buildingRepository;
    @Override
    @Transactional
    public Building createBuilding(Building building) {
        building.setCreatedAt(new Date());
        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public Building updateBuildingById(Integer id, Building building) {
        Building existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Building not found with ID: " + id));

        existingBuilding.setBuildingName(building.getBuildingName());
        existingBuilding.setLocation(building.getLocation());
        existingBuilding.setDescription(building.getDescription());
        existingBuilding.setUpdatedAt(new Date());

        return buildingRepository.save(existingBuilding);
    }

    @Override
    public Page<Building> getAllBuildings(Pageable pageable) {
        return buildingRepository.findAll(pageable);
    }
}
