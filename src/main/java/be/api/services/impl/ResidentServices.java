package be.api.services.impl;

import be.api.dto.request.ResidentRegistrationDTO;
import be.api.model.Apartment;
import be.api.model.Resident;
import be.api.model.User;
import be.api.repository.IApartmentRepository;
import be.api.repository.IResidentRepository;
import be.api.repository.IUserRepository;
import be.api.services.IResidentServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResidentServices implements IResidentServices {

    private static final Logger logger = LoggerFactory.getLogger(ResidentServices.class);
    private final IResidentRepository residentRepository;
    private final IUserRepository userRepository;
    private final IApartmentRepository apartmentRepository;

    public ResidentServices(IResidentRepository residentRepository, IUserRepository userRepository, IApartmentRepository apartmentRepository) {
        this.residentRepository = residentRepository;
        this.userRepository = userRepository;
        this.apartmentRepository = apartmentRepository;
    }

//    @Override
//    public Resident createResident(ResidentRegistrationDTO residentData) {
//        try {
//            // Create and populate the User entity from residentData
//            User user = new User();
//            user.setUsername(residentData.getUsername()); // Set username from email or a separate field
//            user.setEmail(residentData.getEmail());
//            user.setPassword(new BCryptPasswordEncoder().encode(residentData.getPassword()));
//            user.setPhoneNumber(residentData.getPhoneNumber());
//            user.setRole(User.UserRole.ROLE_RESIDENT);
//            user.setFirstName(residentData.getFirstName());
//            user.setLastName(residentData.getLastName());
//            user.setIsActive(true);
//            user.setEmailConfirmed(true);
//
//            // Save the User to the database
//            user = userRepository.save(user);
//
//            Apartment apartment = apartmentRepository.findById(residentData.getApartmentId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid apartment ID"));
//
//            // Create and populate the Resident entity
//            Resident resident = new Resident();
//            resident.setUser(user);  // Associate Resident with the saved User
//            resident.setRewardPoints(residentData.getRewardPoints());
//            resident.setApartment(apartment);
//
//            // Save Resident and return
//            return residentRepository.save(resident);
//        } catch (Exception e) {
//            logger.error("Error creating resident: {}", e.getMessage());
//            return null;
//        }
//    }

    @Override
    public Resident createResident(ResidentRegistrationDTO residentData) {
        try {
//             Validate resident code
            if (residentData.getResidentCode() == null || residentData.getResidentCode().isEmpty()) {
                throw new IllegalArgumentException("Resident code is required");
            }

            // Create and populate the User entity from residentData
            User user = new User();
            user.setUsername(residentData.getUsername());
            user.setEmail(residentData.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(residentData.getPassword()));
            user.setPhoneNumber(residentData.getPhoneNumber());
            user.setRole(User.UserRole.ROLE_RESIDENT);
            user.setFirstName(residentData.getFirstName());
            user.setLastName(residentData.getLastName());
            user.setIsActive(true);
            user.setEmailConfirmed(true);

            // Save the User to the database
            user = userRepository.save(user);

            // Find apartment based on residentCode and phoneNumber
            Apartment apartment = apartmentRepository.findByResidentCodeAndPhoneNumber(
                    residentData.getResidentCode(), residentData.getPhoneNumber());

            if (apartment == null) {
                throw new IllegalArgumentException("Invalid resident code or phone number");
            }

            // Create and populate the Resident entity
            Resident resident = new Resident();
            resident.setUser(user);  // Associate Resident with the saved User
            resident.setRewardPoints(resident.getRewardPoints());  // Set reward points, default to 0
            resident.setApartment(apartment);  // Set the apartment based on residentCode

            // Save Resident and return
            return residentRepository.save(resident);
        } catch (Exception e) {
            logger.error("Error creating resident: {}", e.getMessage());
            return null;
        }
    }


    @Override
    public Page<Resident> getPaginateResident(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return residentRepository.findAll(pageable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("Error fetching paginated residents: {}", e.getMessage());
            return Page.empty();
        }
    }
}