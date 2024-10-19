package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLAScheduleDto {

    private long id;
    private SLACalculationMethodsDto calculationMethodsId;
    private String description;
    private boolean active;
    private String frecuency;
    private int executionTime;
    private Date lastExecution;
}
