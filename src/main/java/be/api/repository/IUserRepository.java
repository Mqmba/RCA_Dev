package be.api.repository;

import be.api.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> searchByName(@Param("name") String name, Pageable pageable);
    User findByEmail(@NotBlank String email);
    Page<User> findByRole(User.UserRole role, Pageable pageable);
    User findByUsername(@NotBlank String username);
    User findByPhoneNumber(@NotBlank String phoneNumber);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long getTotalAccountByRole(User.UserRole role);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") User.UserRole role);
}
