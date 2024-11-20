package be.api.repository;

import be.api.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IApartmentRepository extends JpaRepository<Apartment, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Apartment a SET a.name = :name, a.description = :description WHERE a.apartmentId = :apartmentId")
    Apartment updateApartmentById(@Param("apartmentId") Integer apartmentId,
                            @Param("name") String name,
                            @Param("description") String description);
}
