package be.api.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PaymentHistory")
public class Payment_History extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PaymentHistoryId")
    private int id;

    @Column(name = "OrderCode")
    private String orderCode;


    @Column(name = "PaymentStatus")
    private int paymentStatus;


    @Column(name = "NumberPoint")
    private int numberPoint;

    @ManyToOne
    @JoinColumn(name = "UserId",  referencedColumnName = "UserId")
    private User user;

}
