package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SLACalculationMethodsObjectiveRepository extends JpaRepository<SLACalculationMethodsObjective, Long> {

    List<SLACalculationMethodsObjective> findByCalculationMethodsId(long calculationMethodsId);

    static final String getcmoByIdType = "select " +
            "* " +
            "from " +
            "sla_calculation_methods_objective cmo " +
            "where " +
            "cmo.calculation_methods_id = :id and cmo.type = :px";

    @Query(value = getcmoByIdType, nativeQuery = true)
    SLACalculationMethodsObjective getByCalculationMethodsIdAndType(@Param("id") Long id, @Param("px") String px);

}
