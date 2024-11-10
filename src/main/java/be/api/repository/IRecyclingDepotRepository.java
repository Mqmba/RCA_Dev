package be.api.repository;

import be.api.model.RecyclingDepot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRecyclingDepotRepository extends JpaRepository<RecyclingDepot, Integer> {
    Page<RecyclingDepot> findByIsWorkingTrue(Pageable pageable);
}
