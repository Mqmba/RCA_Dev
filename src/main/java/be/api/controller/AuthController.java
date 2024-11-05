package be.api.controller;

import be.api.dto.request.*;
import be.api.dto.response.AuthResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.UserResponseDTO;
import be.api.exception.ResourceConflictException;
import be.api.model.User;
import be.api.services.impl.AuthService;
import be.api.security.CustomUserDetailsService;
import be.api.security.JwtTokenUtil;
import be.api.services.impl.TokenBlackListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthService authService;
    private final TokenBlackListService tokenBlackListService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseData<?> authenticate(@RequestBody AuthRequestDTO request) {
        try {
            logger.info("Attempting to authenticate user: {}", request.getUsername());
            return authService.authenticate(request);
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        }
    }

    @PostMapping("/logout")
    public ResponseData<?> logout(@RequestBody AuthRequestDTO request) {
        logger.info("Attempting logout for username: {}", request.getUsername());
        return authService.logout(request);
    }

    @PostMapping("/register/resident")
    public ResponseData<?> registerResident(@Valid @RequestBody ResidentRegistrationDTO residentDto) {
        try {
            UserResponseDTO userResponse = authService.registerResident(residentDto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Resident registration successful", userResponse);
        } catch (ResourceConflictException e) {
            return new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Resident registration failed");
        }
    }

    @PostMapping("/register/collector")
    public ResponseData<?> registerCollector(@Valid @RequestBody CollectorRegistrationDTO collectorDto) {
        try {
            UserResponseDTO userResponse = authService.registerCollector(collectorDto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Collector registration successful", userResponse);
        } catch (ResourceConflictException e) {
            return new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Collector registration failed");
        }
    }

    @PostMapping("/register/recycling-depot")
    public ResponseData<?> registerRecyclingDepot(@Valid @RequestBody RecyclingDepotRegistrationDTO recyclingDepotDto) {
        try {
            UserResponseDTO userResponse = authService.registerRecyclingDepot(recyclingDepotDto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Recycling depot registration successful", userResponse);
        } catch (ResourceConflictException e) {
            return new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Recycling depot registration failed");
        }
    }


}