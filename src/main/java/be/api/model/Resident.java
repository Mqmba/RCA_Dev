package be.api.model;

import jakarta.persistence.*;
import lombok.*;

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
    private int rewardPoints = 0;

    @OneToOne
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ApartmentId", referencedColumnName = "ApartmentId")
    private Apartment apartment;
}
