package be.api.services.impl;

import be.api.dto.request.MaterialRequestDTO;
import be.api.model.Material;
import be.api.repository.IMaterialRepository;
import be.api.services.IMaterialServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaterialServices implements IMaterialServices {

    public final IMaterialRepository materialRepository;
    public final ModelMapper modelMapper;
    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material getMaterialById(int id) {
        return null;
    }

    @Override
    public Material addMaterial(MaterialRequestDTO material) {
        Material material1 = modelMapper.map(material, Material.class);
        materialRepository.save(material1);
        log.info("Material added successfully");
        return material1;
    }

    @Override
    public Material updateMaterial(Material material) {
        return null;
    }

    @Override
    public void deleteMaterial(int id) {

    }
}
