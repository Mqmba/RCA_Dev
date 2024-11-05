package be.api.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

// Đây là class DTO (Data Transfer Object) dùng để nhận dữ liệu từ client

@Data
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "Name is not blank")
    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @NotNull(message = "Age is required")
    private Integer age;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)

    private Date updatedAt;

}
