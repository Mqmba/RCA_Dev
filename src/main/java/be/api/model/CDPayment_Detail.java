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
@Table(name = "CDPayment_Detail")
public class CDPayment_Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CDPaymentDetailId")
    private int id;

    @Column(name="CDPaymentId")
    private int cdPaymentId;

    @ManyToOne
    @JoinColumn(name="MaterialId", referencedColumnName = "MaterialId")
    @JsonManagedReference
    private Material material;

    @Column(name="Quantity")
    private double quantity;
}
