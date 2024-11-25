package be.api.controller;

import be.api.dto.request.*;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.UserResponseDTO;
import be.api.exception.ResourceConflictException;
import be.api.services.impl.AuthService;
import be.api.security.CustomUserDetailsService;
import be.api.security.JwtTokenUtil;
import be.api.services.impl.TokenBlackListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping("/login-admin")
    public ResponseData<?> authenticateAdmin(@RequestBody AuthRequestDTO request) {
        try {
            logger.info("Attempting to authenticate admin: {}", request.getUsername());
            return authService.loginAdmin(request);
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        }
    }


    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Invalidates the current JWT token",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {}, required = false)
    )
    @Parameters({
            @Parameter(
                    name = "Authorization",
                    in = ParameterIn.HEADER,
                    required = true,
                    description = "Bearer token",
                    schema = @Schema(type = "string", format = "bearer")
            )
    })
    public ResponseData<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Attempting logout");

        // Pass the token to the service
        return authService.logout(authorizationHeader);
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