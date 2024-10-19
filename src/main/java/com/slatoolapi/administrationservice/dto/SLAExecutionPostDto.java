package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLAExecutionPostDto {

    private long calculationMethodsId;
    private String email;
    private long userId;
    private String date;
    private String time;
    private Boolean isAudit;
}
