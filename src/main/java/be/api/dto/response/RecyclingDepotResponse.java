package be.api.dto.response;

import be.api.model.DepotMaterial;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RecyclingDepotResponse  implements Serializable {

    public int id;
    public  String depotName;
    public  String location;
    public  double latitude;
    public  double longitude;
    public  Boolean isWorking;
    public  double balance;
    public List<DepotMaterialRes> depotMaterials;

    @Data
    public static class DepotMaterialRes {
        public int materialId;
        public String materialName;
        public String materialDescription;
        public double price;
        public boolean isActive;
    }
}


