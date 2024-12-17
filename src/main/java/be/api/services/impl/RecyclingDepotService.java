package be.api.services.impl;

import be.api.dto.request.CreateDepotRequestDTO;
import be.api.dto.response.RecyclingDepotResponse;
import be.api.model.DepotMaterial;
import be.api.model.Material;
import be.api.model.RecyclingDepot;
import be.api.model.User;
import be.api.repository.IDepotMaterialRepository;
import be.api.repository.IMaterialRepository;
import be.api.repository.IRecyclingDepotRepository;
import be.api.repository.IUserRepository;
import be.api.services.IRecyclingDepotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RecyclingDepotService implements IRecyclingDepotService {

    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final IUserRepository userRepository;
    private final IDepotMaterialRepository  depotMaterialRepository;
    private final ModelMapper modelMapper;
    private final IMaterialRepository materialRepository;


    private static final Logger logger = LoggerFactory.getLogger(RecyclingDepotService.class);

    @Override
    public Page<RecyclingDepot> getRecyclingDepots(Pageable pageable) {
        return recyclingDepotRepository.findAll(pageable);
    }

    @Override
    public List<RecyclingDepot> getActiveRecyclingDepots() {
        return recyclingDepotRepository.findByIsWorkingTrue();
    }

    @Override
    public RecyclingDepot createRecyclingDepot(CreateDepotRequestDTO dto) {
        Optional<User> user = userRepository.findById(dto.userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        RecyclingDepot recyclingDepot = new RecyclingDepot();
        recyclingDepot.setDepotName(dto.depotName);
        recyclingDepot.setLocation(dto.location);
        recyclingDepot.setIsWorking(true);
        recyclingDepot.setLatitude(dto.latitude);
        recyclingDepot.setLongitude(dto.longitude);
        recyclingDepot.setUser(user.get());
        recyclingDepot.setBalance(1000);

        return recyclingDepotRepository.save(recyclingDepot);
    }

    @Override
    public RecyclingDepot changeIsWorkingStatus(int id, boolean isWorking) {
        Optional<RecyclingDepot> depotOpt = recyclingDepotRepository.findById(id);
        if (depotOpt.isPresent()) {
            RecyclingDepot depot = depotOpt.get();
            depot.setIsWorking(isWorking);
            return recyclingDepotRepository.save(depot);
        }
        throw new RuntimeException("RecyclingDepot not found with id: " + id);
    }

    @Override
    public RecyclingDepot updateWorkingDate(int id) {
        Optional<RecyclingDepot> depotOpt = recyclingDepotRepository.findById(id);
        if (depotOpt.isPresent()) {
            RecyclingDepot depot = depotOpt.get();
            depot.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            return recyclingDepotRepository.save(depot);
        }
        throw new RuntimeException("RecyclingDepot not found with id: " + id);
    }

    @Override
    public List<RecyclingDepot> getListRecyclingDepots() {
        return recyclingDepotRepository.findAll();
    }

    @Override
    public RecyclingDepotResponse getRecyclingDepotById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        RecyclingDepot depot =  user.getRecyclingDepot();
        RecyclingDepotResponse res = modelMapper.map(depot, RecyclingDepotResponse.class);
        List<DepotMaterial> depotMaterials = depotMaterialRepository.findByRecyclingDepotId(depot.getId());
        List<RecyclingDepotResponse.DepotMaterialRes> depotMaterialResList = depotMaterials.stream().map(material -> {
            RecyclingDepotResponse.DepotMaterialRes materialRes = new RecyclingDepotResponse.DepotMaterialRes();
            materialRes.setMaterialId(material.getMaterial().getId());
            materialRes.setMaterialName(material.getMaterial().getName());
            materialRes.setPrice(material.getPrice());
            materialRes.setActive(material.isActive());
            materialRes.setMaterialDescription(material.getMaterial().getDescription());
            return materialRes;
        }).toList();

        // Gán danh sách DepotMaterialRes vào response
        res.setDepotMaterials(depotMaterialResList);
        return res;
    }

    @Override
    public List<Material> checkIsMissingMaterial() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        RecyclingDepot depot = user.getRecyclingDepot();
        List<Material> materials = materialRepository.findAll();
        List<DepotMaterial> depotMaterials = depotMaterialRepository.findByRecyclingDepotId(depot.getId());
        List<Material> missingMaterials = new ArrayList<>();
        for (Material material : materials) {
            boolean isExist = false;
            for (DepotMaterial depotMaterial : depotMaterials) {
                if (material.getId() == depotMaterial.getMaterial().getId()) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                missingMaterials.add(material);
            }
        }
        return missingMaterials;
    }


}
