package be.api.services.impl;

import be.api.dto.request.ApartmentRequestDTO;
import be.api.model.Apartment;
import be.api.repository.IApartmentRepository;
import be.api.services.IApartmentServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApartmentService implements IApartmentServices {

    private static final Logger logger = LoggerFactory.getLogger(ApartmentService.class);
    private final IApartmentRepository apartmentRepository;

    public ApartmentService(IApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public Apartment createApartment(ApartmentRequestDTO apartmentDTO) {
        try {
            Apartment apartment = new Apartment();
            apartment.setApartmentNumber(apartmentDTO.getApartmentNumber());  // Map from DTO
            apartment.setPhoneNumber(apartmentDTO.getPhoneNumber());          // Map from DTO
            apartment.setFloor(apartmentDTO.getFloor());                      // Map from DTO
            apartment.setResidentCode(apartmentDTO.getResidentCode());        // Map from DTO

            return apartmentRepository.save(apartment);
        } catch (Exception e) {
            logger.error("Error while creating apartment: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Apartment updateApartment(ApartmentRequestDTO apartmentDTO) {
        try {
            apartmentRepository.updateApartmentById(
                    apartmentDTO.getApartmentId(),
                    apartmentDTO.getApartmentNumber(),
                    apartmentDTO.getFloor(),
                    apartmentDTO.getResidentCode(),
                    apartmentDTO.getPhoneNumber()
            );

            // Retrieve and return the updated apartment (optional)
            return apartmentRepository.findById(apartmentDTO.getApartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Apartment not found with ID: " + apartmentDTO.getApartmentId()));

        } catch (Exception e) {
            logger.error("Error while updating apartment: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Page<Apartment> getAllApartments(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return apartmentRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error while retrieving apartments: {}", e.getMessage());
            return Page.empty();
        }
    }
}