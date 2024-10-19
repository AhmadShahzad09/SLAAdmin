package com.slatoolapi.administrationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_METER_STAGING", schema = "public")
public class SLADataMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String priority;
    private String serial_number;
    private String zone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date process_date;
    private String organization;
    private String status;
    private String meter_type;
    private Integer load_id;
    private String meter_model;
}
