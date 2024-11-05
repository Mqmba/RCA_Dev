package be.api.repository;

import be.api.model.Collector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectorRepository extends JpaRepository<Collector, Integer> {
}
