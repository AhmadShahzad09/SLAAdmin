package com.slatoolapi.administrationservice.dto;

import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import lombok.Data;
import java.util.List;

@Data
public class SLACalculationMethodsPutDto {
    private SLACalculationMethods slaCalculationMethods;
    private List<SLACalculationMethodsObjective> slaCalculationMethodsObjectives;

}
