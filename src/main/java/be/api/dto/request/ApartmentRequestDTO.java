package be.api.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentRequestDTO {
    private int apartmentId;
    private String apartmentNumber;  // Assuming apartmentNumber corresponds to "name"
    private String phoneNumber;      // Assuming phoneNumber corresponds to "description"
    private int floor;
    private String residentCode;
    private int buildingId; // If you're setting building too
}
