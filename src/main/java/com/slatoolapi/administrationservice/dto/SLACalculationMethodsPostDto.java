package com.slatoolapi.administrationservice.dto;

import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLACalculationMethodsPostDto {
    private SLACalculationMethods slaCalculationMethods;
    private List<SLACalculationMethodsObjective> slaCalculationMethodsObjectives;
}
