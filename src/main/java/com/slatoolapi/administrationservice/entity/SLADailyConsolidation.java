package com.slatoolapi.administrationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SLA_DAILY_CONSOLIDATION", schema = "public")
public class SLADailyConsolidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long sla_id;
    private String sla_name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sla_date;
    private Float overall_achieved;
    private Float p1_achieved;
    private Float p2_achieved;
    private Float p3_achieved;
    private Float total_meters_overall;
    private Float total_meters_p1;
    private Float total_meters_p2;
    private Float total_meters_p3;
    private String zone;
    private Boolean p1_success;
    private Boolean p2_success;
    private Boolean p3_success;
    private Boolean overall_success;
    private String type;
    private String type_uom_p1;
    private String type_uom_p2;
    private String type_uom_p3;
    private String type_uom_overall;

    @ManyToOne
    private SLACalculationMethods scmet;

}
