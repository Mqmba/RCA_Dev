package be.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Schedule")
public class Schedule extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleId")
    private int scheduleId;

    @Column(name = "ScheduleDate")
    private Date scheduleDate;

    @Column(name = "MaterialType", columnDefinition = "TEXT")
    private String materialType;

    public enum scheduleStatus {
        PENDING,
        ACCEPTED,
        SUCCESS,
        CANCELED
    }

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private scheduleStatus status = scheduleStatus.PENDING;

    @Column(name="CollectorNote", columnDefinition = "TEXT", nullable = true)
    private String collectorNote;

    @Column(name="ResidentNote", columnDefinition = "TEXT", nullable = true)
    private String residentNote;

    @ManyToOne
    @JoinColumn(name = "BuildingId", referencedColumnName = "buildingId")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "RecyclingDepotId", referencedColumnName = "recyclingDepotId")
    @JsonManagedReference
    private RecyclingDepot recyclingDepot;

    @ManyToOne
    @JoinColumn(name = "CollectorId", referencedColumnName = "CollectorId")
    @JsonManagedReference
    private Collector collector;

    @ManyToOne
    @JoinColumn(name = "ResidentId", referencedColumnName = "ResidentId")
    @JsonManagedReference
    private Resident residentId;

}
