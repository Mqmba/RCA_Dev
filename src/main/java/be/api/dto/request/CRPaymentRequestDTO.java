package be.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CRPaymentRequestDTO implements Serializable {

    @NotNull(message = "ScheduleId is required")
    private Integer scheduleId;

    @NotEmpty(message = "Materials list cannot be empty")
    private List<MaterialDTO> materials;

    @Data
    public static class MaterialDTO {

        @NotNull(message = "MaterialId is required")
        private Integer materialId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
