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
public class AdminDashboardResponseDTO {
    public long numberAccountDepot;
    public long numberAccountCollector;
    public long numberAccountResident;
    public long numberTransaction;
    public List<Resident>  topListResident;


}
