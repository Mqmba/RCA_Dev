package be.api.dto.response;

import be.api.model.Resident;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminActivityResponseDTO {
    public long numberTransaction;
    public long numberAccountResident;
    public double avgTransactionPoint;
    public List<Resident> residentList;

}
