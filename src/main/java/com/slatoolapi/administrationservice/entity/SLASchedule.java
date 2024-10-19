package com.slatoolapi.administrationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_SCHEDULE", schema = "public")
public class SLASchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long calculationMethodsId;
    private String description;
    private boolean active;
    private String frecuency;
    private int executionTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastExecution;

    @ManyToOne
    private SLACalculationMethods scmet;

    @OneToOne
    private SLAExecution se;
}
