package be.api.repository;

import be.api.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMaterialRepository extends JpaRepository<Material, Integer> {
}
