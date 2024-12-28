package be.api.dto.request;

import lombok.Data;
import java.io.Serializable;
@Data
public class CreateMaterialTypeRequestDTO implements Serializable {
    private String name;
}
