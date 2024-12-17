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
@Table(name = "UserVoucher")
public class UserVoucher extends  AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserVoucherId")
    private int userVoucherId;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "UserId")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "VoucherId", referencedColumnName = "VoucherId")
    @JsonManagedReference
    private Voucher voucher;


    @Column(name = "isUsed")
    private boolean isUsed;
}
