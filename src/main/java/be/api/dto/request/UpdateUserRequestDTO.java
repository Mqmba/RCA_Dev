package be.api.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateUserRequestDTO  implements Serializable {
    public String firstName;
    public String lastName;
}
