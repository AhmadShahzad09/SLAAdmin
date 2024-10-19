package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLAExecutionDto {

    private long id;
    private SLAScheduleDto scheduleId;
    private String slaName;
    private Integer userId;
    private String email;
    private String status;
    private LocalDate slaDate;
    private String description;
    private String type;
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

}
