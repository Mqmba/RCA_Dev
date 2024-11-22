package be.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Collector")
public class Collector extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CollectorId")
    private int collectorId;

    @Column(name = "VehicleType")
    private Integer vehicleType;

    @Column(name = "VehicleLicensePlate")
    private String vehicleLicensePlate;

    @Column(name = "Rate")
    private Integer rate;

    @Column(name = "NumberPoint")
    private double numberPoint;

    @Column(name = "IsWorking")
    private Boolean isWorking;

    @Column(name = "balance")
    private Double balance;

    @OneToOne
    @JoinColumn(name = "UserId", nullable = false, foreignKey = @ForeignKey(name = "FK_Collector_User"))
    @JsonBackReference
    private User user;
}
