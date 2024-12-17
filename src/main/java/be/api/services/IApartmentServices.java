package be.api.services;

import be.api.dto.request.ApartmentRequestDTO;
import be.api.model.Apartment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IApartmentServices {
    Apartment createApartment(ApartmentRequestDTO apartment);
    Apartment updateApartment(ApartmentRequestDTO apartment);
    Page<Apartment> getAllApartments(int page, int size);
}
