package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLACalculationMethodsDto {

    private long id;
    private String name;
    private String description;
    private SLACalculationModuleDto calculationModuleId;
    private String slaType;
    private String plannedExecutionTime;
    private List<SLACalculationMethodObjectiveDto> slaCalculationMethodObjectiveDtos;

}
