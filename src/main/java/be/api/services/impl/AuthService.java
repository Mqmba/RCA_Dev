package be.api.services.impl;

import be.api.dto.request.*;
import be.api.dto.response.AuthResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.UserResponseDTO;
import be.api.exception.ResourceConflictException; // Custom exception for conflict scenarios
import be.api.model.Collector;
import be.api.model.RecyclingDepot;
import be.api.model.Resident;
import be.api.model.User;
import be.api.repository.ICollectorRepository;
import be.api.repository.IRecyclingDepotRepository;
import be.api.repository.IResidentRepository;
import be.api.repository.IUserRepository;
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

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

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

    public ResponseData<?> logout(AuthRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Logout failed: User not authenticated");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "User not authenticated");
        }

        String authenticatedUsername = authentication.getName();
        if (!authenticatedUsername.equals(request.getUsername())) {
            logger.warn("Logout failed: Provided username {} does not match authenticated username {}", request.getUsername(), authenticatedUsername);
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Username does not match authenticated user");
        }

        String token = (String) authentication.getCredentials();
        if (token == null) {
            logger.warn("Logout failed: No token found in authentication credentials");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "No token found");
        }

        if (tokenBlackListService.isTokenBlacklisted(token)) {
            logger.warn("Logout failed: Token has already been blacklisted");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token has already been blacklisted");
        }

        tokenBlackListService.setTokenBlacklist(token, 3600);
        SecurityContextHolder.clearContext();

        logger.info("Logout successful for username: {}", authenticatedUsername);
        return new ResponseData<>(HttpStatus.OK.value(), "Logout successful", authenticatedUsername);
    }

    public UserResponseDTO registerResident(ResidentRegistrationDTO residentDto) {
        try {
            validateUserDetails(residentDto.getEmail(), residentDto.getUsername(), residentDto.getPhoneNumber());

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
                    .apartment(null)
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
                    .isWorking(collectorDto.getIsWorking())
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
                    .isWorking(recyclingDepotDto.getIsWorking())
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
}
