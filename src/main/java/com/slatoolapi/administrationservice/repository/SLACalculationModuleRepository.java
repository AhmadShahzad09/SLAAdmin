package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLACalculationModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SLACalculationModuleRepository extends JpaRepository<SLACalculationModule, Long> {

}
