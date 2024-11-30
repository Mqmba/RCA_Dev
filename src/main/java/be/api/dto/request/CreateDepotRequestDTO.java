package be.api.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateDepotRequestDTO implements Serializable {
    public int userId;
    public String depotName;
    public String location;
    public double latitude;
    public double longitude;

}
