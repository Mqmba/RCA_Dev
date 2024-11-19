package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CollectorDepot_Payment")
public class CollectorDepotPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CollectorDepotPaymentId")
    private int cdPaymentId;
    @Column(name = "Amount")
    private int amount;
    @Column(name = "PaymentStatus")
    private int status;
    @ManyToOne
    @JoinColumn(name = "CollectorId")
    private Collector collector;
    @ManyToOne
    @JoinColumn(name = "RecyclingDepotId")
    private RecyclingDepot depot;
}
