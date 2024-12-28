package be.api.repository;

import be.api.model.Material;
import be.api.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMaterialTypeRepository extends JpaRepository<MaterialType, Integer> {
}
