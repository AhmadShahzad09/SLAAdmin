package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLAExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SLAExecutionRepository
                extends JpaRepository<SLAExecution, Long>, JpaSpecificationExecutor<SLAExecution> {

        List<SLAExecution> findByScheduleId(long scheduleId);

        static final String resultExecutionDaily = "select " +
                        "* " +
                        "from " +
                        "sla_execution se " +
                        "where " +
                        "se.status <> 'done' and (:scheduleId IS NULL OR se.schedule_id = :scheduleId);";

        @Query(value = resultExecutionDaily, nativeQuery = true)
        List<SLAExecution> findByScheduleIdStatus(@Param("scheduleId") long scheduleId);

        static final String resultExecution = "select * " +
                        "from sla_execution sl " +
                        "where CAST(sl.sla_date AS Date) >= CAST(:monthlyStart AS Date) " +
                        "and CAST(sl.sla_date AS Date) <= CAST(:monthlyEnd AS Date) " +
                        "and sl.sla_name = :name " +
                        "and (:zone IS NULL OR sl.zone = CAST(:zone AS character varying)) " +
                        "and sl.start_date_time = ( " +
                        "select max(sl1.start_date_time) " +
                        "from sla_execution sl1 " +
                        "where CAST(sl1.sla_date AS Date) = CAST(sl.sla_date AS Date) " +
                        "and sl1.sla_name = sl.sla_name " +
                        "and (:zone IS NULL OR sl1.zone = CAST(:zone AS character varying))) " +
                        "order by sl.sla_date";

        @Query(value = resultExecution, nativeQuery = true)
        List<SLAExecution> getResultExecution(@Param("name") String name,
                        @Param("monthlyStart") String monthlyStart,
                        @Param("monthlyEnd") String monthlyEnd,
                        @Param("zone") String zone);

        static final String resultExecutionZone = "select DISTINCT sl.zone " +
                        "from sla_execution sl " +
                        "where CAST(sl.sla_date AS Date) >= CAST(:monthlyStart AS Date) " +
                        "and CAST(sl.sla_date AS Date) <= CAST(:monthlyEnd AS Date) " +
                        "and sl.sla_name = :name " +
                        "and sl.zone IS NOT NULL";

        @Query(value = resultExecutionZone, nativeQuery = true)
        List<String> getResultExecutionZone(@Param("name") String name,
                        @Param("monthlyStart") String monthlyStart,
                        @Param("monthlyEnd") String monthlyEnd);

        static final String resultUnique = "select * " +
                        "from " +
                        "sla_execution se " +
                        "where se.sla_name = :name " +
                        "and se.sla_date = cast(:date as Date)" +
                        "and se.execution_result = 'ok' " +
                        "limit '1';";

        @Query(value = resultUnique, nativeQuery = true)
        SLAExecution getResultUnique(@Param("name") String name, @Param("date") String date);

        @Query(value = "select * from public.sla_execution e WHERE true=true " +
                        "AND e.audit = :bAudit " +
                        "AND (e.sla_name = :sla or :sla is null or :sla = '') " +
                        "AND (e.status = :status or :status is null or :status = '') " +
                        "AND (e.email = :email or :email = '' or :email = '') " +
                        "AND (e.execution_result = :result or :result is null or :result = '') " +
                        "AND (e.sla_date = CAST(:slaDate as Date) or CAST(:slaDate as Date) is null or :slaDate = '')" +
                        "AND (e.start_date_time >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') "
                        +
                        "AND (e.end_date_time <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '')", nativeQuery = true)
        Page<SLAExecution> findAllFiltersTesting(@Param("sla") String sla,
                        @Param("status") String status,
                        @Param("result") String result,
                        @Param("slaDate") String slaDate,
                        @Param("startDate") String startDate,
                        @Param("endDate") String endDate, @Param("email") String email,
                        Pageable pageable,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "select * from public.sla_execution e WHERE true=true " +
                        "AND e.audit = :bAudit " +
                        "AND (e.sla_name = :sla or :sla is null or :sla = '') " +
                        "AND (e.email = :email or :email = '' or :email = '') " +
                        "AND (e.status = :status or :status is null or :status = '') " +
                        "AND (e.execution_result = :result or :result is null or :result = '') " +
                        "AND (e.sla_date = CAST(:slaDate as Date) or CAST(:slaDate as Date) is null or :slaDate = '') "
                        +
                        "AND (e.start_date_time >= CAST(:startDate as timestamp) or CAST(:startDate as timestamp) is null or :startDate = '') "
                        +
                        "AND (e.end_date_time <= CAST(:endDate as timestamp) or CAST(:endDate as timestamp) is null or :endDate = '') "
                        +
                        "AND e.sla_date <= (date_trunc('month', now()) + interval '1 month - 1 day') " +
                        "AND e.sla_date >= (date_trunc('month', current_date))", nativeQuery = true)
        Page<SLAExecution> findAllFiltersTestingMonthly(@Param("sla") String sla,
                        @Param("status") String status,
                        @Param("result") String result,
                        @Param("slaDate") String slaDate,
                        @Param("startDate") String startDate,
                        @Param("endDate") String endDate,
                        @Param("email") String email,
                        Pageable pageable,
                        @Param("bAudit") Boolean bAudit);

        @Query(value = "select * from public.sla_execution e WHERE '1'='1' " +
                        "AND e.audit = :bAudit " +
                        "AND e.sla_date <= (date_trunc('month', now()) + interval '1 month - 1 day') " +
                        "AND e.sla_date >= (date_trunc('month', current_date))", nativeQuery = true)
        Page<SLAExecution> findLastMonthly(Pageable pageable, @Param("bAudit") Boolean bAudit);

        @Query(value = "select * from public.sla_execution e WHERE '1'='1' " +
                        "AND e.audit = :bAudit", nativeQuery = true)
        Page<SLAExecution> findAllWA(Pageable pageable, @Param("bAudit") Boolean bAudit);

        List<SLAExecution> findBySlaNameAndSlaDateBetween(String slaName, LocalDate startDate, LocalDate endDate);

        
}