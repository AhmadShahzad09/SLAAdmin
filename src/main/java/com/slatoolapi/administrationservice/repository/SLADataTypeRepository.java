package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLADataType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SLADataTypeRepository extends JpaRepository<SLADataType, Long> {
}
