package be.api.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Voucher")
public class Voucher extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VoucherId")
    private int voucherId;

    @Column(name = "Name")
    private String name;


    @Column(name = "Description")
    private String description;

    @Column(name = "VoucherCode")
    private String voucherCode;

    @Column(name = "PointToRedeem")
    private double pointToRedeem;


}
