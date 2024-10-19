package com.slatoolapi.administrationservice.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class SLAAuditDto {
    private LocalDate processDate;
    private String priority;
    private String organization;
    private String serialNumber;
    private String status;
    private String zone;
    private String meterType;
    private String orderName;
    private String orderStatus;
    private OffsetDateTime datetime;
    private OffsetDateTime inittime;
    private OffsetDateTime finishtime;
}
