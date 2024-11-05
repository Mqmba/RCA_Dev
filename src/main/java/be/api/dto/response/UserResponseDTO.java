package be.api.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
}