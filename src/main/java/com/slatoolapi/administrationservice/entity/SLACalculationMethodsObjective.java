package com.slatoolapi.administrationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_CALCULATION_METHODS_OBJECTIVE", schema = "public")
public class SLACalculationMethodsObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long calculationMethodsId;
    private String type;
    private float target;
    private String targetUOM;
    private String compare;
    private Integer limitTimes;
    private float numberOfTimes;
    private float evaluationTimes;
    private String evaluationTimesUOM;
    private float consecutiveTimes;
    private String consecutiveTimesUOM;

    @ManyToOne
    private SLACalculationMethods scmet;
}
