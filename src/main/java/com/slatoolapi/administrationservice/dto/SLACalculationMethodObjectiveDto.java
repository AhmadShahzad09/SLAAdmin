package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLACalculationMethodObjectiveDto {

    private long id;
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
}
