package be.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class MaterialRequestDTO implements Serializable {

    @NotNull(message = "Name is required")
    public String name;
    public String description;

    @NotNull(message = "Price is required")
    public double price;
}
