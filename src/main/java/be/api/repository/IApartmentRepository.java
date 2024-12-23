package be.api.repository;

import be.api.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IApartmentRepository extends JpaRepository<Apartment, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Apartment a SET a.apartmentNumber = :apartmentNumber, a.floor = :floor, a.residentCode = :residentCode, a.phoneNumber = :phoneNumber WHERE a.apartmentId = :apartmentId")
    void updateApartmentById(@Param("apartmentId") Integer apartmentId,
                             @Param("apartmentNumber") String apartmentNumber,
                             @Param("floor") Integer floor,
                             @Param("residentCode") String residentCode,
                             @Param("phoneNumber") String phoneNumber);

    Apartment findByResidentCodeAndPhoneNumber(String residentCode, String phoneNumber);

    Apartment findByResident_ResidentId(int residentId);

    List<Apartment> findByBuilding_BuildingId(int buildingId);
}
