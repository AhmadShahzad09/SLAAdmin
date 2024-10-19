package com.slatoolapi.administrationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_DATA_ENTRY", schema = "public")
public class SLADataEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long dataTypeId;
    private float value;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private String comments;

    @ManyToOne
    private SLADataType sdt;
}
