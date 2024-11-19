package be.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ResidentReward")
public class ResidentReward extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResidentRewardId")
    private int residentRewardId;

    @ManyToOne
    @JoinColumn(name = "ResidentId", nullable = false, foreignKey = @ForeignKey(name = "FK_ResidentReward_Resident"))
    private Resident resident;

    @ManyToOne
    @JoinColumn(name = "RewardId", nullable = false, foreignKey = @ForeignKey(name = "FK_ResidentReward_Reward"))
    private Reward reward;

    @Column(name = "Status")
    private int status;
}
