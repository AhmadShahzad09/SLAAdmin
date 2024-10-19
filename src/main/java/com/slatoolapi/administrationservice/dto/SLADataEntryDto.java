package com.slatoolapi.administrationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SLADataEntryDto {

    private long id;
    private SLADataTypeDto dataTypeId;
    private float value;
    private Date businessStartDate;
    private Date businessEndDate;
    private Date createDate;
    private String comments;
}
