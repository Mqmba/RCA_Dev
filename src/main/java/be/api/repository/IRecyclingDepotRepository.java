package be.api.repository;

import be.api.model.RecyclingDepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecyclingDepotRepository extends JpaRepository<RecyclingDepot, Integer> {
}
