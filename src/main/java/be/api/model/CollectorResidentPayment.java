package be.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private double amountPoint;

    @Column(name = "PaymentStatus")
    private int status;

    @OneToOne
    @JoinColumn(name = "ScheduleId", referencedColumnName = "ScheduleId")
    private Schedule schedule;

}
