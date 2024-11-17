package be.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BuildingId")
    private int buildingId;

    @Column(name = "BuildingName", nullable = false, unique = true)
    private String buildingName;

    @Column(name = "Location")
    private String location;  // Ensure you have location field.

    @Column(name = "Description")
    private String description;  // Ensure you have description field.

    @Column(name = "CreatedAt")
    private Date createdAt;  // Ensure you have createdAt field.

    @Column(name = "UpdatedAt")
    private Date updatedAt;  // Ensure you have updatedAt field.

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apartment> apartments;
}
