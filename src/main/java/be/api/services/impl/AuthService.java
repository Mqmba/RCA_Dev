package be.api.services.impl;

import be.api.dto.request.*;
import be.api.dto.response.AuthResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.UserResponseDTO;
import be.api.exception.ResourceConflictException; // Custom exception for conflict scenarios
import be.api.model.*;
import be.api.repository.*;
import be.api.security.CustomUserDetailsService;
import be.api.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final IResidentRepository residentRepository;
    private final ICollectorRepository collectorRepository;
    private final IRecyclingDepotRepository recyclingDepotRepository;
    private final IApartmentRepository apartmentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenBlackListService tokenBlackListService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public ResponseData<?> authenticate(AuthRequestDTO request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null ||
                    request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Username and password must not be null or empty.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

             User userDetails = userDetailsService.loadUserByUsername(request.getUsername());
             String token = jwtTokenUtil.generateToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new ResponseData<>(HttpStatus.OK.value(),
                    "Authentication successful",
                    new AuthResponseDTO(token, roles)
            );
        } catch (BadCredentialsException e) {
            logger.warn("Authentication error: Invalid username or password");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password");
        }
    }

    public ResponseData<?> loginAdmin(AuthRequestDTO request)
    {
        try {
            if (request.getUsername() == null || request.getPassword() == null ||
                    request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Username and password must not be null or empty.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            if (roles.contains("ROLE_ADMIN")) {
                return new ResponseData<>(HttpStatus.OK.value(),
                        "Authentication successful",
                        new AuthResponseDTO(token, roles)
                );
            } else {
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
            }
        } catch (BadCredentialsException e) {
            logger.warn("Authentication error: Invalid username or password");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password");
        }
    }



    public ResponseData<?> logout(String authorizationHeader) {
        try {
            // Ensure the header contains the Bearer token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Logout failed: Invalid Authorization header");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid Authorization header");
            }

            // Extract the token (the part after "Bearer ")
            String token = authorizationHeader.substring(7); // Removing "Bearer " part

            // Get the authentication object (for any other necessary validations, though token suffices)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Logout failed: User not authenticated");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "User not authenticated");
            }

            // Token validation - check if it's already blacklisted
            if (tokenBlackListService.isTokenBlacklisted(token)) {
                logger.warn("Logout failed: Token has already been blacklisted");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token has already been blacklisted");
            }

            // Blacklist the token
            tokenBlackListService.setTokenBlacklist(token, 3600); // 3600 seconds = 1 hour validity for the blacklisted token

            // Clear the authentication context
            SecurityContextHolder.getContext().setAuthentication(null);

            logger.info("Logout successful for token: {}", token);
            return new ResponseData<>(HttpStatus.OK.value(), "Logout successful", null);
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Logout failed");
        }
    }

    public UserResponseDTO registerResident(ResidentRegistrationDTO residentDto) {
        try {
            validateResidentDetails(residentDto.getEmail(), residentDto.getUsername(), residentDto.getPhoneNumber(), residentDto.getResidentCode());

            // Validate apartment by checking the residentCode and phoneNumber
            Apartment apartment = apartmentRepository.findByResidentCodeAndPhoneNumber(
                    residentDto.getResidentCode(),
                    residentDto.getPhoneNumber()
            );

            // If apartment is not found, throw an exception
            if (apartment == null) {
                throw new ResourceConflictException("Invalid resident code or phone number.");
            }

            User user = createUser(
                    residentDto.getUsername(),
                    residentDto.getPassword(),
                    residentDto.getEmail(),
                    residentDto.getFirstName(),
                    residentDto.getLastName(),
                    residentDto.getPhoneNumber(),
                    User.UserRole.ROLE_RESIDENT
            );

            Resident resident = Resident.builder()
                    .user(user)
                    .rewardPoints(0)
                    .apartment(apartment)
                    .build();

            residentRepository.save(resident);

            return mapToUserResponseDTO(user);
        } catch (Exception e) {
            logger.error("Error registering resident: {}", e.getMessage());
            throw e;
        }
    }

    public UserResponseDTO registerCollector(CollectorRegistrationDTO collectorDto) {
        try {
            validateUserDetails(collectorDto.getEmail(), collectorDto.getUsername(), collectorDto.getPhoneNumber());

            User user = createUser(
                    collectorDto.getUsername(),
                    collectorDto.getPassword(),
                    collectorDto.getEmail(),
                    collectorDto.getFirstName(),
                    collectorDto.getLastName(),
                    collectorDto.getPhoneNumber(),
                    User.UserRole.ROLE_COLLECTOR
            );

            Collector collector = Collector.builder()
                    .user(user)
                    .rate(0)
                    .numberPoint(0)
                    .isWorking(true)
                    .build();

            collectorRepository.save(collector);

            return mapToUserResponseDTO(user);
        } catch (Exception e) {
            logger.error("Error registering collector: {}", e.getMessage());
            throw e;
        }
    }

    public UserResponseDTO registerRecyclingDepot(RecyclingDepotRegistrationDTO recyclingDepotDto) {
        try {
            validateUserDetails(recyclingDepotDto.getEmail(), recyclingDepotDto.getUsername(), recyclingDepotDto.getPhoneNumber());

            User user = createUser(
                    recyclingDepotDto.getUsername(),
                    recyclingDepotDto.getPassword(),
                    recyclingDepotDto.getEmail(),
                    recyclingDepotDto.getFirstName(),
                    recyclingDepotDto.getLastName(),
                    recyclingDepotDto.getPhoneNumber(),
                    User.UserRole.ROLE_RECYCLING_DEPOT
            );

            RecyclingDepot recyclingDepot = RecyclingDepot.builder()
                    .user(user)
                    .depotName(recyclingDepotDto.getDepotName())
                    .location(recyclingDepotDto.getLocation())
                    .isWorking(true)
                    .build();


            recyclingDepotRepository.save(recyclingDepot);

            return mapToUserResponseDTO(user);
        } catch (Exception e) {
            logger.error("Error registering recycling depot: {}", e.getMessage());
            throw e;
        }
    }


    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setRole(user.getRole().name());
        return userResponse;
    }

    private User createUser(String username, String password, String email,
                            String firstName, String lastName, String phoneNumber,
                            User.UserRole role) {
        User user = User.builder()  // Changed to use builder pattern
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .emailConfirmed(true)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    private void validateResidentDetails(String email, String username, String phoneNumber, String residentCode) {
        if (userRepository.findByEmail(email) != null) {
            throw new ResourceConflictException("Email is already in use.");
        }

        if (userRepository.findByUsername(username) != null) {
            throw new ResourceConflictException("Username is already in use.");
        }

        // Validate phone number and resident code combination in the Apartment repository
        Apartment apartment = apartmentRepository.findByResidentCodeAndPhoneNumber(residentCode, phoneNumber);
        if (apartment == null) {
            throw new ResourceConflictException("Invalid resident code or phone number.");
        }

        // Validate phone number and resident code formats
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new ResourceConflictException("Invalid phone number format.");
        }

        if (residentCode != null && !isValidResidentCode(residentCode)) {
            throw new ResourceConflictException("Invalid resident code format.");
        }
    }

    private void validateUserDetails(String email, String username, String phoneNumber) {
        if (userRepository.findByEmail(email) != null) {
            throw new ResourceConflictException("Email is already in use.");
        }

        if (userRepository.findByUsername(username) != null) {
            throw new ResourceConflictException("Username is already in use.");
        }

        if (userRepository.findByPhoneNumber(phoneNumber) != null) {
            throw new ResourceConflictException("Phone number is already in use.");
        }
    }



    // Simple validation for phone number format (can be customized as needed)
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Example check: Ensure the phone number is 10 digits (you can adjust this as per your requirements)
        return phoneNumber.matches("\\d{10}");
    }

    // Simple validation for resident code format (customize as needed)
    private boolean isValidResidentCode(String residentCode) {
        // Example check: Ensure resident code follows a specific pattern, like "RES" followed by 3 digits
        return residentCode.matches("^RES\\d{3}$");
    }
}
