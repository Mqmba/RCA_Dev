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
@Table(name = "Schedule")
public class Schedule extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleId")
    private Integer scheduleId;

    @Column(name = "ScheduleDate")
    private Date scheduleDate;

    @Column(name = "MaterialType")
    private String materialType;

    @Column(name = "Status")
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "BuildingId", referencedColumnName = "buildingId")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "RecyclingDepotId", referencedColumnName = "recyclingDepotId")
    private RecyclingDepot recyclingDepot;

    @ManyToOne
    @JoinColumn(name = "CollectorId", referencedColumnName = "CollectorId")
    private Collector collector;
}
