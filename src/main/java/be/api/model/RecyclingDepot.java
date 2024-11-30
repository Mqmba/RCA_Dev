package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RecyclingDepot")
public class RecyclingDepot extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecyclingDepotId")
    private int id;


    @Column(name = "DepotName")
    private String depotName;

    @Column(name = "Location")
    private String location;

    @Column(name="Latitude")
    private double latitude;

    @Column(name="Longitude")
    private double longitude;

    @Column(name = "IsWorking")
    private Boolean isWorking;

    @Column(name = "Balance")
    private double balance;

    @OneToOne
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "userId")
    private User user;

}
