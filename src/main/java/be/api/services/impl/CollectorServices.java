package be.api.services.impl;

import be.api.model.Apartment;
import be.api.model.Collector;
import be.api.repository.ICollectorRepository;
import be.api.services.ICollectorServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CollectorServices implements ICollectorServices {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CollectorServices.class);
    private final ICollectorRepository collectorRepository;

    public CollectorServices(ICollectorRepository collectorRepository) {
        this.collectorRepository = collectorRepository;
    }

    @Override
    public Page<Collector> getAllCollectors(int page, int size) {
            try {
                Pageable pageable = PageRequest.of(page, size);
                return collectorRepository.findAll(pageable);
            } catch (Exception e) {
                logger.error("Error while retrieving apartments: {}", e.getMessage());
                return Page.empty();
            }
    }

    @Override
    public Collector updateCollector(boolean isWorking,int collectorId) {
        try{
            Collector collector = collectorRepository.findById(collectorId)
                    .orElseThrow(() -> new IllegalArgumentException("Collector not found with ID: " + collectorId));
            collector.setIsWorking(isWorking);
            return collectorRepository.save(collector);
        }catch (Exception e){
            logger.error("Error while updating collector: {}", e.getMessage());
        }
        return null;
    }
}
