package be.api.controller;

import be.api.model.Building;
import be.api.services.impl.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping("/create-building")
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }

    @PutMapping("/update-building-by-id")
    public ResponseEntity<Building> updateBuilding(@RequestParam Integer id, @RequestBody Building building) {
        return ResponseEntity.ok(buildingService.updateBuildingById(id, building));
    }

    @GetMapping("/get-list-building-by-paging")
    public ResponseEntity<Page<Building>> getAllBuildings(Pageable pageable) {
        return ResponseEntity.ok(buildingService.getAllBuildings(pageable));
    }
}
