package be.api.services.impl;

import be.api.dto.request.CreateDepotRequestDTO;
import be.api.model.RecyclingDepot;
import be.api.model.User;
import be.api.repository.IRecyclingDepotRepository;
import be.api.repository.IUserRepository;
import be.api.services.IRecyclingDepotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RecyclingDepotService implements IRecyclingDepotService {

    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final IUserRepository userRepository;

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


}
