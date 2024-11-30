package be.api.repository;

import be.api.model.RecyclingDepot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecyclingDepotRepository extends JpaRepository<RecyclingDepot, Integer> {
    List<RecyclingDepot> findByIsWorkingTrue();

    RecyclingDepot findByUser_Username(String username);
}
