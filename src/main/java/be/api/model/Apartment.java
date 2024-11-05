package be.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @Column(name = "ApartmentId")  // Matches database schema
    private int apartmentId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;
}
