package be.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CDPaymentRequestDTO implements Serializable {


    @NotEmpty(message = "Materials list cannot be empty")
    public List<CRPaymentRequestDTO.MaterialDTO> materials;

    @NotNull(message = "CollectorId is required")
    public int collectorId;

 


    @NotNull(message = "TotalPrice is required")
    public String materialType;

    @Data
    public static class MaterialDTO {

        @NotNull(message = "MaterialId is required")
        private Integer materialId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
