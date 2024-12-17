package be.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Resident")
public class Resident extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResidentId")
    private int residentId;

    @Column(name = "RewardPoints")
    private double rewardPoints = 0;

    @OneToOne
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "ApartmentId", nullable = false, referencedColumnName = "ApartmentId")
    private Apartment apartment;


}
