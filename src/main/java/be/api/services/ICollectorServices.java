package be.api.services;

import be.api.model.Collector;
import org.springframework.data.domain.Page;

public interface ICollectorServices {
    Page<Collector> getAllCollectors(int page, int size);
    Collector updateCollector(boolean isWorking, int collectorId);
}
