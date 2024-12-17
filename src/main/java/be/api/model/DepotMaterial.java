package be.api.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DepotMaterial")
public class DepotMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DepotMaterialId")
    private int id;

    @ManyToOne
    @JoinColumn(name="RecyclingDepotId", referencedColumnName = "RecyclingDepotId")
    @JsonManagedReference
    private RecyclingDepot recyclingDepot;

    @ManyToOne
    @JoinColumn(name="MaterialId", referencedColumnName = "MaterialId")
    @JsonManagedReference
    private Material material;

    @Column (name="Price")
    private double price;

    @Column (name="IsActive")
    private boolean isActive = true;

}
