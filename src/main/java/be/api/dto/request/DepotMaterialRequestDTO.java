package be.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DepotMaterialRequestDTO {

    @NotEmpty(message = "Materials list cannot be empty")
    public List<MaterialDataDTO> materials;

    @Data
    public static class MaterialDataDTO {
        @NotNull(message = "MaterialId is required")
        private Integer materialId;

        @NotNull(message = "Price is required")
        private double price;

        @NotNull(message = "IsActive is required")
        private boolean isActive;
    }
}
