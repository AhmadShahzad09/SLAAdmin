package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLAExecution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SLAExecutionRepositoryPageable extends PagingAndSortingRepository<SLAExecution, Long> {

    List<SLAExecution> findAllBySlaName(String slaName, Pageable pageable);

    List<SLAExecution> findAllByStatus(String slaStatus, Pageable pageable);

    List<SLAExecution> findAllByExecutionResult(String executionResult, Pageable pageable);

    List<SLAExecution> findAllBySlaDate(LocalDate slaDate, Pageable pageable);

    List<SLAExecution> findAllByStartDateTime(LocalDateTime startDateTime, Pageable pageable);

    List<SLAExecution> findAllByEndDateTime(LocalDateTime endDateTime, Pageable pageable);

    List<SLAExecution> findAllByZone(String zone, Pageable pageable);
}
