package be.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private Integer buildingId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Location")
    private String location;

    @Column(name = "Description")
    private String description;

    @Column(name = "CreateAt")
    private Date createdAt;

    @Column(name = "UpdateAt")
    private Date updatedAt;
}
