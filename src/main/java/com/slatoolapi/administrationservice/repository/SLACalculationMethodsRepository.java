package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SLACalculationMethodsRepository extends JpaRepository<SLACalculationMethods, Long> {
    static final String extractExisting = "select " +
            "* " +
            "from " +
            "sla_calculation_methods as sc;";

    @Query(value = extractExisting, nativeQuery = true)
    List<SLACalculationMethods> getExtractExisting();

    @Query(value = "select * from sla_calculation_methods WHERE name = :sla", nativeQuery = true)
    SLACalculationMethods slaCalculationMethods(@Param("sla") String sla);

}
