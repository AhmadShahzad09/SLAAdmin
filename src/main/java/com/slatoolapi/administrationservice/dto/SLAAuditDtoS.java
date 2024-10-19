package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class SLAAuditDtoS {
    private String processDate;  
    private String priority;
    private String organization;
    private String serialNumber;
    private String status;
    private String zone;
    private String meterType;
    private String orderName;
    private String orderStatus;
    private String datetime; 
    private String inittime;
    private String finishtime; 
}
