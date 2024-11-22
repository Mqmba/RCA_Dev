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
@Table(name = "CRPayment_Detail")
public class CRPayment_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CRPaymentDetailId")
    private int id;

    @Column(name="CRPaymentId")
    private int crPaymentId;

    @ManyToOne
    @JoinColumn(name="MaterialId", referencedColumnName = "MaterialId")
    @JsonManagedReference
    private Material material;

    @Column(name="Quantity")
    private double quantity;

}
