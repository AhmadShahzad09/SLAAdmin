package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.dto.SLAAuditDtoS;
import com.slatoolapi.administrationservice.entity.SLASchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface SLAScheduleRepository extends JpaRepository<SLASchedule, Long> {
    Optional<SLASchedule> findByCalculationMethodsId(long calculationMethodsId);

    @Query(value = "select * from sla_schedule as schedule, sla_calculation_methods as methods WHERE true=true " +
            "AND (schedule.calculation_methods_id = methods.id) " +
            "AND (methods.name = :sla or :sla is null or :sla = '')", nativeQuery = true)
    List<SLASchedule> findScheduleBySla(@Param("sla") String sla);

    @Query(value = "select * from sla_schedule ", nativeQuery = true)

    List<SLASchedule> queryPruebaAudit();

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "NULL as order_name, NULL as order_status, NULL as datetime, NULL as inittime, NULL as finishtime " +
            "from sla_meter_staging ms " +
            "where ms.process_date  = CAST(:fecha AS Timestamp) " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryNoCommandAudit(
            @Param("fecha") String fecha);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "NULL as order_name, NULL as order_status, NULL as datetime, NULL as inittime, NULL as finishtime " +
            "from sla_meter_staging ms " +
            "where ms.process_date  = CAST(:fecha AS Timestamp) " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryNoCommandAuditStream(
            @Param("fecha") String fecha);

    @Query(value = "SELECT " +
            "'Only Data Entry' as process_date, 'Only Data Entry' as priority, 'Only Data Entry' as organization, " +
            "'Only Data Entry' as serial_number, 'Only Data Entry' as status, 'Only Data Entry' as zone, " +
            "'Only Data Entry' as meter_type,  'Only Data Entry' as order_name, 'Only Data Entry' as order_status, " +
            "'Only Data Entry' as datetime,  'Only Data Entry' as inittime, 'Only Data Entry' as finishtime", nativeQuery = true)

    Stream<Object[]> queryDataEntryAuditStream();
    
    @Query(value = "SELECT " +
            "'Only Data Entry' as process_date, 'Only Data Entry' as priority, 'Only Data Entry' as organization, " +
            "'Only Data Entry' as serial_number, 'Only Data Entry' as status, 'Only Data Entry' as zone, " +
            "'Only Data Entry' as meter_type,  'Only Data Entry' as order_name, 'Only Data Entry' as order_status, " +
            "'Only Data Entry' as datetime,  'Only Data Entry' as inittime, 'Only Data Entry' as finishtime", nativeQuery = true)

    List<Object[]> queryDataEntryAudit();

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit1Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit1(
            @Param("fecha") String fecha,
            @Param("c1") String c1);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit13(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit13Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
            +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit18(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13,
            @Param("c14") String c14,
            @Param("c15") String c15,
            @Param("c16") String c16,
            @Param("c17") String c17,
            @Param("c18") String c18);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
            +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit18Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13,
            @Param("c14") String c14,
            @Param("c15") String c15,
            @Param("c16") String c16,
            @Param("c17") String c17,
            @Param("c18") String c18);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, c40, :c41) "
            +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit41(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13,
            @Param("c14") String c14,
            @Param("c15") String c15,
            @Param("c16") String c16,
            @Param("c17") String c17,
            @Param("c18") String c18,
            @Param("c17") String c19,
            @Param("c18") String c20,
            @Param("c1") String c21,
            @Param("c2") String c22,
            @Param("c3") String c23,
            @Param("c4") String c24,
            @Param("c5") String c25,
            @Param("c6") String c26,
            @Param("c7") String c27,
            @Param("c8") String c28,
            @Param("c9") String c29,
            @Param("c10") String c30,
            @Param("c11") String c31,
            @Param("c12") String c32,
            @Param("c13") String c33,
            @Param("c14") String c34,
            @Param("c15") String c35,
            @Param("c16") String c36,
            @Param("c17") String c37,
            @Param("c18") String c38,
            @Param("c17") String c39,
            @Param("c18") String c40,
            @Param("c18") String c41

    );
    
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			"and cs.datetime >=  cast(:fecha AS Timestamp) " +
			"and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, c40, :c41) "
            +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit41Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4,
            @Param("c5") String c5,
            @Param("c6") String c6,
            @Param("c7") String c7,
            @Param("c8") String c8,
            @Param("c9") String c9,
            @Param("c10") String c10,
            @Param("c11") String c11,
            @Param("c12") String c12,
            @Param("c13") String c13,
            @Param("c14") String c14,
            @Param("c15") String c15,
            @Param("c16") String c16,
            @Param("c17") String c17,
            @Param("c18") String c18,
            @Param("c17") String c19,
            @Param("c18") String c20,
            @Param("c1") String c21,
            @Param("c2") String c22,
            @Param("c3") String c23,
            @Param("c4") String c24,
            @Param("c5") String c25,
            @Param("c6") String c26,
            @Param("c7") String c27,
            @Param("c8") String c28,
            @Param("c9") String c29,
            @Param("c10") String c30,
            @Param("c11") String c31,
            @Param("c12") String c32,
            @Param("c13") String c33,
            @Param("c14") String c34,
            @Param("c15") String c35,
            @Param("c16") String c36,
            @Param("c17") String c37,
            @Param("c18") String c38,
            @Param("c17") String c39,
            @Param("c18") String c40,
            @Param("c18") String c41

    );
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
			  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit2(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
			  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit2Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
			  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit4(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4);
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
			  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3, :c4) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    Stream<Object[]> queryCommandAudit4Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("c4") String c4);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
			  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
			  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2) " +
            "and cs.organization = ms.organization " +
            "order by 1,2", nativeQuery = true)

    List<Object[]> queryCommandAudit5(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2);

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "order by 1,2 DESC LIMIT 100", nativeQuery = true)

    List<SLAAuditDtoS> queryCommandAudit1x();

    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "order by 1,2 DESC LIMIT 100", nativeQuery = true)

    List<Object[]> queryCommandAuditPrueba();
    
    @Query(value = "select CAST(ms.process_date AS Date) as process_date, " +
            "ms.priority, " +
            "ms.organization, " +
            "ms.serial_number, " +
            "ms.status, " +
            "ms.zone, " +
            "ms.meter_type, " +
            "cs.order_name, " +
            "cs.order_status, " +
            "cs.datetime, " +
            "cs.inittime, " +
            "cs.finishtime " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
            "and cs.datetime >=  cast(:fecha AS Timestamp) " +
            "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3) " +
            "and cs.organization = ms.organization " +
            "order by 1,2 LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> queryCommandAudit5Stream(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3,
            @Param("limit") int limit,
            @Param("offset") int offset);

    
    @Query(value = "select count(*) " +
            "from sla_command_staging cs, sla_meter_staging ms " +
            "where ms.serial_number = cs.device_name " +
            "and ms.process_date = CAST(:fecha AS Timestamp) " +
            "and cs.datetime >=  cast(:fecha AS Timestamp) " +
            "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
            "and cs.order_name in (:c1, :c2, :c3) " +
            "and cs.organization = ms.organization ", nativeQuery = true)
    int queryCountCommandAudit5(
            @Param("fecha") String fecha,
            @Param("c1") String c1,
            @Param("c2") String c2,
            @Param("c3") String c3);

}
