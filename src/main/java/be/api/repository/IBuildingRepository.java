package be.api.repository;

import be.api.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBuildingRepository extends JpaRepository<Building, Integer> {
}
