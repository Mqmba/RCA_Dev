package be.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CollectorDepot_Payment")
public class CollectorDepotPayment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CollectorDepotPaymentId")

    private int cdPaymentId;
    @Column(name = "Amount")
    private double amount;


    public enum CollectorDepotPaymentStatus {
        PENDING,
        SUCCESS,
        CANCELED
    }

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private CollectorDepotPaymentStatus status = CollectorDepotPaymentStatus.PENDING;

    @Column(name = "MaterialType", columnDefinition = "TEXT")
    private String materialType;

    @ManyToOne
    @JoinColumn(name = "CollectorId", nullable = false, referencedColumnName = "CollectorId")
    @JsonManagedReference
    private Collector collector;

    @ManyToOne
    @JoinColumn(name = "RecyclingDepotId", referencedColumnName = "RecyclingDepotId")
    @JsonManagedReference
    private RecyclingDepot recyclingDepot;
}
