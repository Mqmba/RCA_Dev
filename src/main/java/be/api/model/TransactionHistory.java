package be.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TransactionHistory")
public class TransactionHistory extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionId")
    private int transactionId;

    @Column(name = "NumberPoint")
    private double numberPoint;

    @Column(name = "OrderCode")
    private String orderCode;


    public enum TransactionType {
        PENDING,
        SUCCESS,
        CANCELED
    }
    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private TransactionType status = TransactionType.PENDING;

    @ManyToOne
    @JoinColumn(name = "SenderId", referencedColumnName = "UserId")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "ReceiverId")
    @JsonManagedReference
    private User receiverId;


}
