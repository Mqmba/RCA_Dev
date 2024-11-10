package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CollectorResident_Payment")
public class CollectorResidentPayment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CRPaymentId")
    private int crPaymentId;
    @Column(name = "AmountPoint")
    private int amountPoint;
    @Column(name = "PaymentStatus")
    private int status;
    @ManyToOne
    private Resident resident;
    @ManyToOne
    private Collector collector;
}
