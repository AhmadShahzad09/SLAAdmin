package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLAExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationResultRepository extends JpaRepository<SLAExecution, Long> {


    // a partir de acá para el calculationResultController
    @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
            "AND e.audit = false " +
            "AND " +
            "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
            "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
            "AND " +
            "(e.type = :type or :type is null or :type = '') " +
            "AND " +
            "(e.sla_date >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') " +
            "AND " +
            "(e.sla_date <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '') " +
            "ORDER BY e.sla_date DESC, e.sla_name ASC",
            nativeQuery = true) // todos los filtros (estrictamente fecha debe ir), sin tildado del check de los ultimos
    Page<SLAExecution> findWithoutLastExecution(Pageable pageable,
                                       @Param("zone") String zone, @Param("sla") String sla,
                                       @Param("type") String type,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate);

                                    

            @Query(value = "SELECT * " +
                            "FROM sla_execution execution " +
                            "WHERE true=true " +
                            "AND (:sla is null or :sla = '' or execution.sla_name = :sla) " +
                            "AND (:zone is null or :zone = '' or execution.zone = :zone) " +
                            "AND (:type is null or :type = '' or execution.type = :type) " +
                            "AND (:startDate = '' or :startDate is null or execution.sla_date >= CAST(:startDate as timestamp)) " +
                            "AND (:endDate = '' or :endDate is null or execution.sla_date <= CAST(:endDate as timestamp)) " +
                            "AND execution.audit = false " +
                            "and execution.execution_result = 'ok' " +
                            "AND execution.sla_date = ( " +
                            "SELECT MAX(s.sla_date) FROM sla_execution s " +
                            "WHERE true=true " +
                            "AND (s.sla_name = execution.sla_name) " +
                            "AND (:zone is null or :zone = '' or s.zone = execution.zone) " +
                            "AND (:type is null or :type = '' or s.type = execution.type) " +
                            "AND s.audit = false " +
                            "and s.execution_result = 'ok' " +
                            "AND (:startDate = '' or :startDate is null or s.sla_date >= CAST(:startDate as timestamp)) " +
                            "AND (:endDate = '' or :endDate is null or s.sla_date <= CAST(:endDate as timestamp))) " +
                            "ORDER BY execution.sla_date DESC, execution.sla_name ASC", nativeQuery = true)
            Page<SLAExecution> findWithLastExecution(Pageable pageable,
                             @Param("zone") String zone,
                             @Param("sla") String sla,
                            @Param("type") String type,
                            @Param("startDate") String startDate,
                            @Param("endDate") String endDate);









/* 
"where CAST(sl1.sla_date AS Date) = CAST(sl.sla_date AS Date) " +
@Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
            "AND " +
            "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') " +
            "AND " +
            "(e.type = :type or :type is null or :type = '') " +
            "AND " +
            "(e.sla_date >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') " +
            "AND " +
            "(e.sla_date <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '')",
            nativeQuery = true) // todos los filtros (estrictamente fecha debe ir), sin tildado del check de los ultimos
    Page<SLAExecution> findWithoutLastExecution(Pageable pageable,
                                       @Param("sla") String sla,
                                       @Param("type") String type,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate); */
                                


        /*  @Query(value = "SELECT execution.* " +
            "FROM sla_execution execution " +
            "WHERE execution.status='done' " + 
            "AND (:sla IS NULL OR :sla = '' OR execution.sla_name = :sla) " +
            "AND (:type IS NULL OR :type = '' OR execution.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR execution.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR execution.sla_date <= CAST(:endDate AS timestamp)) " +
            "AND execution.sla_date = (" +
            "SELECT MAX(s.sla_date) " +
            "FROM sla_execution s " +
            "WHERE true=true " +
            "AND (:sla IS NULL OR :sla = '' OR s.sla_name = :sla) " +
            "AND (:type IS NULL OR :type = '' OR s.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR s.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR s.sla_date <= CAST(:endDate AS timestamp)) " +
            ") " +
            "ORDER BY execution.sla_date DESC",
            nativeQuery = true) // todos los filtros (estrictamente fecha debe ir), con tildado del check
    Page<SLAExecution> findWithLastExecution(Pageable pageable,
                                                @Param("sla") String sla,
                                                @Param("type") String type,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate); */




    







         @Query(value = "SELECT execution.* " +
            "FROM sla_execution execution " +
            "WHERE execution.status='done' " +
            "AND execution.audit = :bAudit " +
            "AND (:sla IS NULL OR :sla = '' OR execution.sla_name = :sla) " +
             "AND (:zone is null or :zone = '' or execution.zone = :zone) " +
             "AND (:type IS NULL OR :type = '' OR execution.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR execution.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR execution.sla_date <= CAST(:endDate AS timestamp)) " +
            "AND execution.sla_date = (" +
            "SELECT MAX(s.sla_date) " +
            "FROM sla_execution s " +
            "WHERE true=true " +
            "AND execution.audit = :bAudit " +
            "AND (:sla IS NULL OR :sla = '' OR s.sla_name = :sla) " +
             "AND (:zone IS NULL OR :zone = '' OR s.zone = :zone) " +
             "AND (:type IS NULL OR :type = '' OR s.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR s.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR s.sla_date <= CAST(:endDate AS timestamp)) " +
            ") " +
            "ORDER BY execution.sla_date DESC, execution.sla_name ASC",
            nativeQuery = true) // todos los filtros (estrictamente fecha debe ir), con tildado del check
    Page<SLAExecution> findWithLastExecutionA(Pageable pageable,
                                                @Param("zone") String zone,
                                                @Param("sla") String sla,
                                                @Param("type") String type,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate,
                                                @Param("bAudit") Boolean bAudit);

           @Query(value = "SELECT execution.* " +
            "FROM sla_execution execution " +
            "WHERE execution.status='done' " +
            "AND execution.audit = :bAudit " +
            "AND (:sla IS NULL OR :sla = '' OR execution.sla_name = :sla) " +
           "AND (:zone IS NULL OR :zone = '' OR execution.zone = :zone) " +
           "AND (:type IS NULL OR :type = '' OR execution.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR execution.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR execution.sla_date <= CAST(:endDate AS timestamp)) " +
            "AND execution.sla_date = (" +
            "SELECT MAX(s.sla_date) " +
            "FROM sla_execution s " +
            "WHERE true=true " +
            "AND execution.audit = :bAudit " +
            "AND (:sla IS NULL OR :sla = '' OR s.sla_name = :sla) " +
           "AND (:zone IS NULL OR :zone = '' OR s.sla_name = :zone) " +
           "AND (:type IS NULL OR :type = '' OR s.type = :type) " +
            "AND (:startDate = '' OR :startDate IS NULL OR s.sla_date >= CAST(:startDate AS timestamp)) " +
            "AND (:endDate = '' OR :endDate IS NULL OR s.sla_date <= CAST(:endDate AS timestamp)) " +
            ") "  +
            "ORDER BY execution.sla_date DESC, execution.sla_name ASC",
            nativeQuery = true) // todos los filtros (estrictamente fecha debe ir), con tildado del check
    Page<SLAExecution> findWithLastExecutionAB(Pageable pageable,
                                                @Param("zone") String zone,
                                                @Param("sla") String sla,
                                                @Param("type") String type,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate,
                                                @Param("bAudit") Boolean bAudit);


    /* @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
            "AND " +
            "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') " +
            "AND " +
            "(e.type = :type or :type is null or :type = '') " +
            "AND " +
            "(e.sla_date > CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') " +
            "AND " +
            "e.sla_date > CAST(:startDate as timestamp)",
            nativeQuery = true) // todos los filtros (estrictamente fecha de start), sin tildado del check de los ultimos
    Page<SLAExecution> findWithoutLastExecutionStartDate(Pageable pageable,
                                              @Param("sla") String sla,
                                              @Param("type") String type,
                                              @Param("startDate") String startDate); */


         @Query(value = "SELECT * " +
                "FROM sla_execution execution " +
                "WHERE true=true " +
                "AND (:sla is null or :sla = '' or execution.sla_name = :sla) " +
                 "AND (:zone is null or :zone = '' or execution.zone = :zone) " +
                 "AND (:type is null or :type = '' or execution.type = :type) " +
                "AND (execution.sla_date >= CAST(:startDate as timestamp)) " +
                "AND execution.audit = false " +
                "and execution.execution_result = 'ok' " +
                "AND execution.sla_date = ( " +
                "SELECT MAX(s.sla_date) FROM sla_execution s " +
                "WHERE true=true " +
                "AND (s.sla_name = execution.sla_name) " +
                 "AND (:zone is null or :zone = '' or s.zone = execution.zone) " +
                 "AND (s.type = execution.type ) " +
                "AND s.audit = false " +
                "and s.execution_result = 'ok' " +
                "AND (s.sla_date >= CAST(:startDate as timestamp))) " +
                "ORDER BY execution.sla_date DESC, execution.sla_name ASC", nativeQuery = true)
Page<SLAExecution> findWithLastExecutionStartDate(Pageable pageable,
                @Param("zone") String zone,
                @Param("sla") String sla,
                @Param("type") String type,
                @Param("startDate") String startDate);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "AND " +
                        "(e.sla_date >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') "
                        +
                        "AND " +
                        "(e.sla_date <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '') "
                        +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionA(Pageable pageable,
                        @Param("zone") String zone, @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("startDate") String startDate,
                        @Param("endDate") String endDate,
                        @Param("bAudit") Boolean bAudit);


        @Query(value = "SELECT * " +
                "FROM sla_execution execution " +
                "WHERE true=true " +
                "AND (:sla is null or :sla = '' or execution.sla_name = :sla) " +
                "AND (:zone is null or :zone = '' or execution.zone = :zone) " +
                "AND (:type is null or :type = '' or execution.type = :type) " +
                "AND (execution.sla_date <= CAST(:endDate as timestamp)) " +
                "AND execution.audit = false " +
                "and execution.execution_result = 'ok' " +
                "AND execution.sla_date = ( " +
                "SELECT MAX(s.sla_date) FROM sla_execution s " +
                "WHERE true=true " +
                "AND (s.sla_name = execution.sla_name) " +
                "AND (:zone is null or :zone = '' or s.zone = execution.zone) " +
                "AND (s.type = execution.type ) " +
                "AND s.audit = false " +
                "and s.execution_result = 'ok' " + 
                "AND (s.sla_date <= CAST(:endDate as timestamp))) " +
                "ORDER BY execution.sla_date DESC, execution.sla_name ASC", nativeQuery = true)
                Page<SLAExecution> findWithLastExecutionEndDate(Pageable pageable,
                                @Param("zone") String zone,
                                @Param("sla") String sla,
                                @Param("type") String type,
                                @Param("endDate") String endDate);

                        @Query(value = "SELECT * " +
                        "FROM sla_execution execution " +
                        "WHERE true=true " +
                        "AND (:sla is null or :sla = '' or execution.sla_name = :sla) " +
                        "AND (:zone is null or :zone = '' or execution.zone = :zone) " +
                        "AND (:type is null or :type = '' or execution.type = :type) " +
                        "AND execution.audit = false " +
                        "and execution.execution_result = 'ok' " +
                        "AND execution.sla_date = ( " +
                        "SELECT MAX(s.sla_date) FROM sla_execution s " +
                        "WHERE true=true " +
                        "AND (s.sla_name = execution.sla_name) " +
                        "AND (s.zone = execution.zone) " +
                        "AND (s.type = execution.type ) " +
                        "AND s.audit = false " +
                        "and s.execution_result = 'ok' " +
                        "ORDER BY execution.sla_date DESC, execution.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithLastExecutionB(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type);



         @Query(value = "SELECT * " +
            "FROM sla_execution e " +
            "WHERE e.execution_result = 'ok' " +
            "AND e.audit = :bAudit " +
            "AND (e.sla_name = :sla OR :sla IS NULL OR :sla = '') " +
             "AND (e.zone = :zone OR :zone IS NULL OR :zone = '') " +
             "AND (e.type = :type OR :type IS NULL OR :type = '') " +
            "AND (e.sla_date = ( " +
            "SELECT MAX(s.sla_date) " +
            "FROM sla_execution s " +
            "WHERE s.sla_date > CAST(:endDate as timestamp) " +
             "AND (s.sla_name = e.sla_name) " +
             "AND (:zone is null or :zone = '' or s.zone = e.zone) " +
             "AND (s.type = e.type ) " +
             "AND s.audit = e.audit " +
             "and s.execution_result = 'ok' ) " +
            ") OR CAST(:endDate as timestamp) IS NULL OR :endDate = '') " +
            "ORDER BY e.sla_date DESC, e.sla_name ASC",
            nativeQuery = true) // todos los filtros (estrictamente fecha de endDate), con tildado del check de los ultimos
    Page<SLAExecution> findWithLastExecutionEndDateA(Pageable pageable,
                                                      @Param("zone") String zone,
                                                      @Param("sla") String sla,
                                                      @Param("type") String type,
                                                      @Param("endDate") String endDate,
                                                      @Param("bAudit") Boolean bAudit);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = false " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "AND " +
                        "(e.sla_date >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') "
                        +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionStartDate(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("startDate") String startDate);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "AND " +
                        "(e.sla_date > CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') "
                        +
                        "AND " +
                        "e.sla_date > CAST(:startDate as timestamp) " +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionStartDateA(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("startDate") String startDate,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "SELECT * " +
                        "FROM sla_execution e " +
                        "WHERE e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit " +
                        "AND (e.sla_name = :sla OR :sla IS NULL OR :sla = '') " +
                        "AND (e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND (e.type = :type OR :type IS NULL OR :type = '') " +
                        "AND (e.sla_date = (" +
                        "SELECT MAX(s.sla_date) " +
                        "FROM sla_execution s " +
                        "WHERE s.sla_date > CAST(:startDate as timestamp) " +
                        "AND (s.sla_name = e.sla_name) " +
                        "AND (s.zone = e.zone) " +
                        "AND (s.type = e.type ) " +
                        "AND s.audit = e.audit " +
                        "and s.execution_result = 'ok' ) " +
                        "OR CAST(:startDate as timestamp) IS NULL OR :startDate = '') " +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithLastExecutionStartDateA(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("startDate") String startDate,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = false " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "AND " +
                        "(e.sla_date <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '') "
                        +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionEndDate(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("endDate") String endDate);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "AND " +
                        "(e.sla_date < CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '') "
                        +
                        "AND " +
                        "e.sla_date < CAST(:endDate as timestamp) " +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionEndDateA(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("endDate") String endDate,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = false " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionNonDate(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type);

        @Query(value = "SELECT * FROM sla_execution e WHERE e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit " +
                        "AND " +
                        "(e.sla_name = :sla OR :sla IS NULL OR :sla = '') AND " +
                        "(e.zone = :zone OR :zone IS NULL OR :zone = '') " +
                        "AND " +
                        "(e.type = :type or :type is null or :type = '') " +
                        "ORDER BY e.sla_date DESC, e.sla_name ASC", nativeQuery = true)
        Page<SLAExecution> findWithoutLastExecutionNonDateA(Pageable pageable,
                        @Param("zone") String zone,
                        @Param("sla") String sla,
                        @Param("type") String type,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "SELECT e.* " +
                        "FROM sla_execution e " +
                        "INNER JOIN (" +
                        "SELECT sla_name, MAX(sla_date) AS max_sla_date " +
                        "FROM sla_execution " +
                        "WHERE execution_result = 'ok' " +
                        "AND (sla_name = :sla OR :sla IS NULL OR :sla = '') " +
                        "AND (type = :type OR :type IS NULL OR :type = '') " +
                        "GROUP BY sla_name" +
                        ") subquery " +
                        "ON e.sla_name = subquery.sla_name AND e.sla_date = subquery.max_sla_date " +
                        "ORDER BY e.sla_date  DESC ", nativeQuery = true)
        Page<SLAExecution> findWithLastExecutionC(Pageable pageable,
                        @Param("sla") String sla,
                        @Param("type") String type);

        @Query(value = "select * from public.sla_execution e WHERE '1'='1' " +
                        "AND e.execution_result = 'ok'", nativeQuery = true)
        Page<SLAExecution> findAllResults(Pageable pageable);

        @Query(value = "select * from public.sla_execution e WHERE '1'='1' " +
                        "AND e.execution_result = 'ok' " +
                        "AND e.audit = :bAudit", nativeQuery = true)
        Page<SLAExecution> findAllResultsA(Pageable pageable, @Param("bAudit") Boolean bAudit);

}
