package be.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "ApartmentNumber")
    private String apartmentNumber;

    @Column(name = "Floor", nullable = false)
    private int floor;

    @Column(name = "ResidentCode")
    private String residentCode;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BuildingId", nullable = false, referencedColumnName = "BuildingId")
    @JsonBackReference
    private Building building;

    @OneToMany(mappedBy = "apartment")
    @JsonBackReference
    private List<Resident> resident;
}
