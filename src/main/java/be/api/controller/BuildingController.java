package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.model.Building;
import be.api.services.impl.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all-building")
    public ResponseData<?>getAllBuildings() {
        try{
            List<Building> buildings = buildingService.getAllBuildings();
            return new ResponseData<>(200, "List of buildings found", buildings);
        }
        catch (Exception e){
            return new ResponseData<>(500, "Internal server error while retrieving list of buildings with message: " + e.getMessage(), null);
        }
    }
}
