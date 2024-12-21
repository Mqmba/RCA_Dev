package be.api.services.impl;

import be.api.exception.ResourceNotFoundException;
import be.api.model.Building;
import be.api.repository.IBuildingRepository;
import be.api.services.IBuildingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
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
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy building: " + id));

        existingBuilding.setBuildingName(building.getBuildingName());
        existingBuilding.setLocation(building.getLocation());
        existingBuilding.setDescription(building.getDescription());
        existingBuilding.setUpdatedAt(new Date());

        return buildingRepository.save(existingBuilding);
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }
}
