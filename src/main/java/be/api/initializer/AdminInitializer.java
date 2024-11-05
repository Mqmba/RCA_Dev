package be.api.initializer;

import be.api.model.Admin;
import be.api.model.User;
import be.api.repository.IAdminRepository;
import be.api.repository.IUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AdminInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(IUserRepository userRepository, IAdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            User adminUser = userRepository.findByUsername("admin");
            if (adminUser != null) {
                logger.info("Admin user already exists. Skipping initialization.");
                return;
            }

            if (userRepository.existsById(1) || adminRepository.existsById(1)) {
                logger.info("Admin user or admin already exists with ID 1. Skipping initialization.");
                return;
            }

            adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.UserRole.ROLE_ADMIN);
            adminUser.setIsActive(true);
            userRepository.save(adminUser); // Save the new admin user with UserId 1
            logger.info("Admin user created successfully with UserId 1: {}", adminUser.getUsername());

            Admin admin = new Admin();
            admin.setUser(adminUser);
            adminRepository.save(admin); // Save the admin instance with AdminId 1
            logger.info("Admin created successfully with AdminId 1");
        } catch (Exception e) {
            logger.error("Error initializing admin user: {}", e.getMessage());
        }
    }
}