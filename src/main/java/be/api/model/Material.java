package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MaterialId")
    private int id;

    @Column(name="Name")
    private String name;

    @Column (name="Description")
    private String description;

    @Column (name="Price")
    private double price;
}
