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
        log.info("Tạo rác thành công");
        return material1;
    }

    @Override
    public Material updateMaterial(Material dto)
    {
        Material exitMaterial = materialRepository.findById(dto.getId()).orElse(null);

        if(exitMaterial != null){
            exitMaterial.setName(dto.getName());
            exitMaterial.setDescription(dto.getDescription());
            exitMaterial.setPrice(dto.getPrice());
            materialRepository.save(exitMaterial);
            log.info("Update rác thành công");
            return exitMaterial;
        }
        return null;
    }

    @Override
    public Boolean deleteMaterial(int id) {
       try{
           materialRepository.deleteById(id);
           return true;
       }
       catch (Exception e){
           return false;
       }
    }
}
