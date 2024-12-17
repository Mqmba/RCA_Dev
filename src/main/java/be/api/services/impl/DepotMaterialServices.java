package be.api.services.impl;

import be.api.dto.request.DepotMaterialRequestDTO;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.model.DepotMaterial;
import be.api.model.Material;
import be.api.model.RecyclingDepot;
import be.api.model.User;
import be.api.repository.IDepotMaterialRepository;
import be.api.repository.IMaterialRepository;
import be.api.repository.IUserRepository;
import be.api.services.IDepotMaterialServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepotMaterialServices implements IDepotMaterialServices {

    private final IDepotMaterialRepository depotMaterialRepository;
    private final IUserRepository userRepository;
    private final IMaterialRepository materialRepository;

    @Override
    public boolean createDepotMaterial(DepotMaterialRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + userName);
        }
        RecyclingDepot depot = user.getRecyclingDepot();
        if (depot == null) {
            throw new ResourceNotFoundException("Recycling depot not found");
        }

        dto.getMaterials().forEach(material -> {
            DepotMaterial depotMaterial = new DepotMaterial();
            DepotMaterial existingDepotMaterial = depotMaterialRepository.findByMaterialIdAndRecyclingDepotId(material.getMaterialId(), depot.getId());
            Material model = materialRepository.findById(material.getMaterialId())
                    .orElseThrow(() -> new IllegalArgumentException("Material not found with ID: " + material.getMaterialId()));
            double price = material.getPrice();

            double priceOriginWith110Percent = model.getPrice() * 1.1;
            if (price < priceOriginWith110Percent) {
                throw new IllegalArgumentException("Price must be greater than or equal to 110% of the original price");
            }
            if(existingDepotMaterial != null){
                existingDepotMaterial.setPrice(price);
                existingDepotMaterial.setActive(material.isActive());
                depotMaterialRepository.save(existingDepotMaterial);

            }
            else{
                depotMaterial.setMaterial(model);
                depotMaterial.setPrice(price);
                depotMaterial.setActive(material.isActive());
                depotMaterial.setRecyclingDepot(depot);
                depotMaterialRepository.save(depotMaterial);
            }

        });

        return true;
    }
}
