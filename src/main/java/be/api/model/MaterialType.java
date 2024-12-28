package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MaterialType")
public class MaterialType  extends AbstractEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MaterialTypeId")
    private int id;

    @Column(name="Name")
    private String name;

}
