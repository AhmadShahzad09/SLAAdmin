package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLADataMeterDto {

    private String serial_number;
    private String status;
    private String zone;
    private String organization;
    private String priority;
    private String order_name;
    private String order_status;
    private OffsetDateTime inittime;
    private OffsetDateTime finishtime;

}
