package com.slatoolapi.administrationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_CALCULATION_METHODS", schema = "public")
public class SLACalculationMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private long calculationModuleId;
    private String slaType;
    private String plannedExecutionTime;

    @OneToMany(mappedBy = "scmet")
    private List<SLACalculationMethodsObjective> scmo;

    @ManyToOne
    private SLACalculationModule scmod;

    @OneToMany(mappedBy = "scmet")
    private List<SLASchedule> ssch;

    @OneToMany(mappedBy = "scmet")
    private List<SLADailyConsolidation> sdcon;
}
