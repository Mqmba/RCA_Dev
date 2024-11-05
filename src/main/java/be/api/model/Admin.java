package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin")
public class Admin extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdminId")
    private int adminId;

    @OneToOne
    @JoinColumn(name = "UserId", nullable = false, foreignKey = @ForeignKey(name = "FK_Admin_User"))
    private User user;
}
