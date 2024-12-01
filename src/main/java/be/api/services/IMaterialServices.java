package be.api.services;

import be.api.dto.request.MaterialRequestDTO;
import be.api.model.Material;

import java.util.List;

public interface IMaterialServices {
    public List<Material> getAllMaterials();
    public Material getMaterialById(int id);
    public Material addMaterial(MaterialRequestDTO material);
    public Material updateMaterial(Material dto);
    public Boolean deleteMaterial(int id);
}
