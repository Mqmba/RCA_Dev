package be.api.services;

import be.api.model.MaterialType;

import java.util.List;

public interface IMaterialTypeServices {
    Boolean createMaterialType(String name);
    Boolean updateMaterialType(Integer id, String name);
    Boolean deleteMaterialType(Integer id);
    List<MaterialType> getAllMaterialType();
    MaterialType getMaterialTypeById(Integer id);
}
