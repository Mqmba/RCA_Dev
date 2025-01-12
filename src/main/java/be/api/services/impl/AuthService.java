package be.api.services.impl;

import be.api.dto.request.*;
import be.api.dto.response.AuthResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.BadRequestException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Không được để trống tên đăng nhập hoặc mật khẩu.");
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
                    "Đăng nhập thành công",
                    new AuthResponseDTO(token, roles)
            );
        } catch (BadCredentialsException e) {
            logger.warn("Authentication error: Invalid username or password");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }

    public ResponseData<?> loginAdmin(AuthRequestDTO request)
    {
        try {
            if (request.getUsername() == null || request.getPassword() == null ||
                    request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Không được để trống tên đăng nhập hoặc mật khẩu.");
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
                        "Đăng nhập thành công",
                        new AuthResponseDTO(token, roles)
                );
            } else {
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Không có quyền truy cập");
            }
        } catch (BadCredentialsException e) {
            logger.warn("Authentication error: Invalid username or password");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }



    public ResponseData<?> logout(String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Logout failed: Invalid Authorization header");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid Authorization header");
            }

            String token = authorizationHeader.substring(7);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Logout failed: User not authenticated");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "User not authenticated");
            }

            if (tokenBlackListService.isTokenBlacklisted(token)) {
                logger.warn("Logout failed: Token has already been blacklisted");
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token has already been blacklisted");
            }

            tokenBlackListService.setTokenBlacklist(token, 3600);
            SecurityContextHolder.getContext().setAuthentication(null);

            return new ResponseData<>(HttpStatus.OK.value(), "Logout successful", null);
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Logout failed");
        }
    }

    public Boolean changePassword(ChangePasswordRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng");
        }

        if (!bCryptPasswordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không đúng");
        }

        user.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);


        return true;
    }

    public Boolean updateInfo(UpdateUserRequestDTO dto) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new BadRequestException("Không tìm thấy người dùng");
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        userRepository.save(user);

        return true;
    }


    public User registerResident(ResidentRegisterRequestDTO dto) {
        validateUserDetails(dto.getEmail(), dto.getUsername(), dto.getPhoneNumber());
        User user = createUser(dto, User.UserRole.ROLE_RESIDENT);
        Resident resident = new Resident();
        resident.setUser(user);
        resident.setRewardPoints(0);
        Optional<Apartment> apartment = apartmentRepository.findById(dto.getApartmentId());
        resident.setApartment(apartment.get());
        residentRepository.save(resident);

        return user;
    }

    public User registerCollector (ResidentRegisterRequestDTO dto) {
        validateUserDetails(dto.getEmail(), dto.getUsername(), dto.getPhoneNumber());
        User user = createUser(dto, User.UserRole.ROLE_COLLECTOR);
        Collector collector = new Collector();
        collector.setUser(user);
        collector.setRate(0);
        collector.setNumberPoint(0);
        collector.setBalance(0.0);
        collectorRepository.save(collector);
        return user;
    }

    public User registerDepot (RegisterDepotRequestDTO dto){
         validateUserDetails(dto.getEmail(), dto.getUsername(), dto.getPhoneNumber());
         User user  = User.builder()
                 .username(dto.getUsername())
                 .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                 .email(dto.getEmail())
                 .firstName(dto.getFirstName())
                 .lastName(dto.getLastName())
                 .address(dto.getAddress())
                 .phoneNumber(dto.getPhoneNumber())
                 .role(User.UserRole.ROLE_RECYCLING_DEPOT)
                 .emailConfirmed(true)
                 .isActive(true)
                 .build();
         User usernew =  userRepository.save(user);

         RecyclingDepot recyclingDepot = new RecyclingDepot();
         recyclingDepot.setUser(usernew);
         recyclingDepot.setDepotName(dto.getDepotName());
         recyclingDepot.setLocation(dto.getLocation());
         recyclingDepot.setIsWorking(true);
         recyclingDepot.setLatitude(dto.getLatitude());
         recyclingDepot.setLongitude(dto.getLongitude());
         recyclingDepot.setBalance(0.0);
         recyclingDepotRepository.save(recyclingDepot);
         return user;

    }



    private User createUser(ResidentRegisterRequestDTO dto, User.UserRole role) {
        User user = User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .role(role)
                .emailConfirmed(true)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    private void validateResidentDetails(String email, String username, String phoneNumber, String residentCode) {
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("Email đã được đăng kí");
        }

        if (userRepository.findByUsername(username) != null) {
            throw new BadRequestException("Số điện thoại đã được đăng kí");
        }

        Apartment apartment = apartmentRepository.findByResidentCodeAndPhoneNumber(residentCode, phoneNumber);
        if (apartment == null) {
            throw new BadRequestException("Mã cư dân hoặc số điện thoại không đúng");
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            throw new BadRequestException("Số điện thoại không hợp lệ");
        }

        if (residentCode != null && !isValidResidentCode(residentCode)) {
            throw new BadRequestException("Mã cư dân không hợp lệ");
        }
    }

    private void validateUserDetails(String email, String username, String phoneNumber) {
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("Email đã được đăng kí");
        }

        if (userRepository.findByUsername(username) != null) {
            throw new BadRequestException("Số điện thoại đã được đăng kí");
        }

        if (userRepository.findByPhoneNumber(phoneNumber) != null) {
            throw new BadRequestException("Số điện thoại đã được đăng kí");
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
