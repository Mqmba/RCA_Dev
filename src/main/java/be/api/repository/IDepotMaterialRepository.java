package be.api.repository;

import be.api.model.DepotMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDepotMaterialRepository extends JpaRepository<DepotMaterial, Integer> {
    DepotMaterial findByMaterialIdAndRecyclingDepotId(int materialId, int depotId);
    List<DepotMaterial> findByRecyclingDepotId(int depotId);
}
