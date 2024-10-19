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
@Table(name = "SLA_DATA_TYPE", schema = "public")
public class SLADataType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "sdt")
    private List<SLACalculationModule> scm;

    @OneToMany(mappedBy = "sdt")
    private List<SLADataEntry> sde;

}
