package com.slatoolapi.administrationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_EXECUTION", schema = "public")
public class SLAExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = true)
    private long scheduleId;
    @Column(nullable = true)
    private Integer userId;
    private String slaName;
    @Column(nullable = true)
    private String status;
    private String email;
    private String description;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate slaDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String executionResult;
    private String zone;
    private float overallAchieved;
    private float overallMetersAffected;
    private float p1Achieved;
    private float p1MetersAffected;
    private float p2Achieved;
    private float p2MetersAffected;
    private float p3Achieved;
    private float p3MetersAffected;
    private Boolean audit;
    private String url;

    @OneToOne(mappedBy = "se")
    private SLASchedule ss;

}