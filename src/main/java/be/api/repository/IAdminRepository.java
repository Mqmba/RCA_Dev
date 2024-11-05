package be.api.repository;

import be.api.model.Admin;
import be.api.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAdminRepository extends JpaRepository<Admin, Integer> {
    boolean existsByUser(User user);
}
