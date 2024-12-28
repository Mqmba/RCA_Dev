package be.api.services.impl;

import be.api.model.MaterialType;
import be.api.repository.IMaterialTypeRepository;
import be.api.services.IMaterialTypeServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaterialTypeServices implements IMaterialTypeServices {

    private final IMaterialTypeRepository materialTypeRepository;

    @Override
    public Boolean createMaterialType(String name) {
        MaterialType materialType = new MaterialType();
        materialType.setName(name);
        materialTypeRepository.save(materialType);
        return true;
    }

    @Override
    public Boolean updateMaterialType(Integer id, String name) {
        MaterialType materialType = materialTypeRepository.findById(id).orElse(null);
        if (materialType == null) {
            return false;
        }
        materialType.setName(name);
        materialTypeRepository.save(materialType);
        return true;
    }

    @Override
    public Boolean deleteMaterialType(Integer id) {
        MaterialType materialType = materialTypeRepository.findById(id).orElse(null);
        if (materialType == null) {
            return false;
        }
        materialTypeRepository.delete(materialType);
        return true;
    }

    @Override
    public List<MaterialType> getAllMaterialType() {
        return materialTypeRepository.findAll();
    }

    @Override
    public MaterialType getMaterialTypeById(Integer id) {
        return materialTypeRepository.findById(id).orElse(null);
    }
}
