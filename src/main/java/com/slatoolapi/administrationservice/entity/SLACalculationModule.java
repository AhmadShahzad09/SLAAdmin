package com.slatoolapi.administrationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SLA_CALCULATION_MODULE", schema = "public")
public class SLACalculationModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long dataTypeId;

    @OneToMany(mappedBy = "scmod")
    private List<SLACalculationMethods> scmet;

    @ManyToOne
    private SLADataType sdt;
}
