package com.slatoolapi.administrationservice.dto;

import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import com.slatoolapi.administrationservice.entity.SLADailyConsolidation;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SLADailyConsolidationDto {

    List<SLADailyConsolidation> dailyConsolidations;

    List<SLACalculationMethodsObjective> slaCalculationMethodsObjective;
}
