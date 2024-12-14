package be.api.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResidentRegisterRequestDTO implements Serializable {
    public String username;

    public String password;

    public String email;

    public String phoneNumber;


    public String firstName;

    public String lastName;

    public String address;
    public int apartmentId;
}
