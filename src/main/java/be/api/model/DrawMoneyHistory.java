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
@Table(name = "DrawMoneyHistory")
public class DrawMoneyHistory  extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DrawMoneyId")
    private int drawMoneyId;

    @Column(name = "NumberPoint")
    private double numberPoint;

    @Column(name = "Amount")
    private double amount;

    public enum STATUS {
        PENDING,
        SUCCESS,
        CANCELED
    }
    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.PENDING;

    private String orderCode;

    // Bank info
    private String bankName;

    // Bank account info
    private String bankAccountName;
    private String bankAccountNumber;

    @ManyToOne
    @JoinColumn(name = "Author", referencedColumnName = "UserId")
    @JsonManagedReference
    private User user;
}
