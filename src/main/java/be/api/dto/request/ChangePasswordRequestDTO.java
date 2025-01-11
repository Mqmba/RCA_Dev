package be.api.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChangePasswordRequestDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
}
