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
@Table(name = "Apartment")
public class Apartment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApartmentId")
    private int apartmentId;

    @Column(name = "ApartmentNumber", nullable = false, unique = true)
    private String apartmentNumber;

    @Column(name = "Floor", nullable = false)
    private int floor;

    @Column(name = "ResidentCode", nullable = false, unique = true)
    private String residentCode;

    @Column(name = "PhoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BuildingId", nullable = false, referencedColumnName = "BuildingId")
    @JsonBackReference
    private Building building;

    @OneToOne(mappedBy = "apartment", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonBackReference
    private Resident resident;
}
