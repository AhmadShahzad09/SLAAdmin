package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLADataMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SLADataMeterRepository extends JpaRepository<SLADataMeter, Long> {

      static final String meterCto = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCto, nativeQuery = true)
      Integer getMeterCountDtoSLA78(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("status") String status, @Param("priority") String priority);

      static final String totalCto = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCto, nativeQuery = true)
      Integer getTotalCountDtoSLA78(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("priority") String priority);

      static final String metersAffectsSLA78 = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA78, nativeQuery = true)
      Integer getMetersAffectsDtoSLA78(@Param("px") String px,
                  @Param("date") String date,
                  @Param("order_name") String order_name);

      static final String meterCtoOv = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCtoOv, nativeQuery = true)
      Integer getMeterCountDtoSLA78Ov(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String totalCtoOv = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCtoOv, nativeQuery = true)
      Integer getTotalCountDtoSLA78Ov(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String metersAffectsSLA78Ov = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA78Ov, nativeQuery = true)
      Integer getMetersAffectsDtoSLA78Ov(@Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date,
                  @Param("order_name") String order_name);

      static final String NetworkAvailabilityActiveMeters = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2)  " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = NetworkAvailabilityActiveMeters, nativeQuery = true)
      Float getNetworkAvailabilityActiveMeters(@Param("status") String status,
                  @Param("status2") String status2,
                  @Param("px") String px,
                  @Param("date") String date);

      static final String NetworkAvailabilityActiveMetersOverall = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2) " +
                  "and ms.process_date=CAST(:date as Timestamp) " +
                  "and ms.priority in (:p1, :p2, :p3)";

      @Query(value = NetworkAvailabilityActiveMetersOverall, nativeQuery = true)
      Float getNetworkAvailabilityActiveMetersOverall(@Param("status") String status,
                  @Param("status2") String status2,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date);

      static final String NetworkAvailabilityBaseLine = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = NetworkAvailabilityBaseLine, nativeQuery = true)
      Float getNetworkAvailabilityBaseLine(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("px") String px,
                  @Param("date") String date);

      static final String NetworkAvailabilityBaseLineOverall = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.process_date=CAST(:date AS Timestamp)";

      @Query(value = NetworkAvailabilityBaseLineOverall, nativeQuery = true)
      Float getNetworkAvailabilityBaseLineOverall(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date);

      static final String NetworkAvailabilityMetersAffects = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.process_date=CAST(:date AS Timestamp) " +
                  "and ms.priority in (:px)";

      @Query(value = NetworkAvailabilityMetersAffects, nativeQuery = true)
      Float getNetworkAvailabilityMetersAffects(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String NetworkAvailabilityMetersAffectsOverall = "select count (*) from sla_meter_staging ms where "
                  +
                  "ms.status in (:param1, :param2, :param3) " +
                  "and ms.process_date=CAST(:date AS Timestamp) " +
                  "and ms.priority in (:p1, :p2, :p3)";

      @Query(value = NetworkAvailabilityMetersAffectsOverall, nativeQuery = true)
      Float getNetworkAvailabilityMetersAffectsOverall(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String NetworkAvailabilityActiveMetersHES = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2)  " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date AS Timestamp)";

      @Query(value = NetworkAvailabilityActiveMetersHES, nativeQuery = true)
      Float getNetworkAvailabilityActiveMetersHES(@Param("status") String status,
                  @Param("status2") String status2,
                  @Param("px") String px,
                  @Param("date") String date);

      static final String NetworkAvailabilityActiveMetersOverallHES = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2)  " +
                  "and ms.process_date=CAST(:date AS Timestamp) " +
                  "and ms.priority in (:p1, :p2, :p3)";

      @Query(value = NetworkAvailabilityActiveMetersOverallHES, nativeQuery = true)
      Float getNetworkAvailabilityActiveMetersOverallHES(@Param("status") String status,
                  @Param("status2") String status2,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date);

      static final String NetworkAvailabilityBaseLineHES = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date AS Timestamp)";

      @Query(value = NetworkAvailabilityBaseLineHES, nativeQuery = true)
      Float getNetworkAvailabilityBaseLineHES(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("px") String px,
                  @Param("date") String date);

      static final String NetworkAvailabilityBaseLineOverallHES = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.process_date=CAST(:date AS Timestamp)";

      @Query(value = NetworkAvailabilityBaseLineOverallHES, nativeQuery = true)
      Float getNetworkAvailabilityBaseLineOverallHES(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date);

      static final String NetworkAvailabilityMetersAffectsHES = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.process_date=CAST(:date AS Timestamp) " +
                  "and ms.priority in (:px)";

      @Query(value = NetworkAvailabilityMetersAffectsHES, nativeQuery = true)
      Float getNetworkAvailabilityMetersAffectsHES(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String NetworkAvailabilityMetersAffectsOverallHES = "select count (*) from sla_meter_staging ms where "
                  +
                  "ms.status in (:param1, :param2, :param3) " +
                  "and ms.process_date=CAST(:date AS Timestamp) " +
                  "and ms.priority in (:p1, :p2, :p3)";

      @Query(value = NetworkAvailabilityMetersAffectsOverallHES, nativeQuery = true)
      Float getNetworkAvailabilityMetersAffectsOverallHES(@Param("param1") String param1,
                  @Param("param2") String param2,
                  @Param("param3") String param3,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String meterCto79 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCto79, nativeQuery = true)
      Float getMeterCountDtoSLA79(@Param("order_name1") String order_name1, @Param("order_name2") String order_name2,
                  @Param("date") String date,
                  @Param("status") String status, @Param("priority") String priority);

      static final String meterCto79Ov = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCto79Ov, nativeQuery = true)
      Float getMeterCountDtoSLA79Ov(@Param("order_name1") String order_name1, @Param("order_name2") String order_name2,
                  @Param("date") String date,
                  @Param("status") String status,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String totalCto79 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCto79, nativeQuery = true)
      Float getTotalCountDtoSLA79(@Param("order_name1") String order_name1, @Param("order_name2") String order_name2,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String totalCto79Ov = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCto79Ov, nativeQuery = true)
      Float getTotalCountDtoSLA79Ov(@Param("order_name1") String order_name1, @Param("order_name2") String order_name2,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String metersAffectsSLA79 = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA79, nativeQuery = true)
      Float getMetersAffectsDtoSLA79(@Param("px") String px,
                  @Param("date") String date,
                  @Param("order_name1") String order_name1, @Param("order_name2") String order_name2);

      static final String metersAffectsSLA79Ov = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:order_name1, :order_name2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA79Ov, nativeQuery = true)
      Float getMetersAffectsDtoSLA79Ov(@Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date,
                  @Param("order_name1") String order_name1, @Param("order_name2") String order_name2);

      static final String meterCto81 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCto81, nativeQuery = true)
      Float getMeterCountDtoSLA81(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("status") String status, @Param("priority") String priority);

      static final String meterCto81Ov = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status =:status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = meterCto81Ov, nativeQuery = true)
      Float getMeterCountDtoSLA81Ov(@Param("order_name") String order_name,
                  @Param("date") String date,
                  @Param("status") String status,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String totalCto81 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCto81, nativeQuery = true)
      Float getTotalCountDtoSLA81(@Param("order_name") String order_name, @Param("date") String date,
                  @Param("priority") String priority);

      static final String totalCto81Ov = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = totalCto81Ov, nativeQuery = true)
      Float getTotalCountDtoSLA81Ov(@Param("order_name") String order_name,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String metersAffectsSLA81 = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA81, nativeQuery = true)
      Float getMetersAffectsDtoSLA81(@Param("px") String px,
                  @Param("date") String date,
                  @Param("order_name") String order_name);

      static final String metersAffectsSLA81Ov = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name =:order_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = metersAffectsSLA81Ov, nativeQuery = true)
      Float getMetersAffectsDtoSLA81Ov(@Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date,
                  @Param("order_name") String order_name);

      static final String AutomaticServiceMeter = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceMeter, nativeQuery = true)
      Integer getAutomaticServiceMeter(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("status") String status,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String AutomaticServiceM = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceM, nativeQuery = true)
      Integer getAutomaticServiceM(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("date") String date,
                  @Param("status") String status,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String AutomaticServiceTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceTotal, nativeQuery = true)
      Integer getAutomaticServiceTotal(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String AutomaticServiceTotalM = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceTotalM, nativeQuery = true)
      Integer getAutomaticServiceTotalM(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String AutomaticServiceAffects = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceAffects, nativeQuery = true)
      Integer getAutomaticServiceAffects(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String AutomaticServiceA = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:command_name, :command_name_two) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = AutomaticServiceA, nativeQuery = true)
      Integer getAutomaticServiceA(@Param("command_name") String command_name,
                  @Param("command_name_two") String command_name_two,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteCDMeter = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDMeter, nativeQuery = true)
      Integer getRemoteCDMeter(@Param("c1") String c1,
                  @Param("status") String status,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemoteCDOMeter = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDOMeter, nativeQuery = true)
      Integer getRemoteCDOMeter(@Param("c1") String c1,
                  @Param("status") String status,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteCDTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDTotal, nativeQuery = true)
      Integer getRemoteCDTotal(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemoteCDOTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDOTotal, nativeQuery = true)
      Integer getRemoteCDOTotal(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteCDAffects = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDAffects, nativeQuery = true)
      Integer getRemoteCDAffects(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String RemoteCDOAffects = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteCDOAffects, nativeQuery = true)
      Integer getRemoteCDOAffects(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String remoteDataAcquisition = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority = :priority " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisition, nativeQuery = true)
      Integer getCommandCount(@Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("status") String status,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String remoteDataAcquisitionTolerance = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionTolerance, nativeQuery = true)
      Integer getRemoteDataAcquisitionTolerance(@Param("c1") String c1,
                  @Param("c2") String c2, @Param("status") String status,
                  @Param("date") String date, @Param("priority") String priority);

      static final String remoteDataAcquisitionToleranceTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionToleranceTotal, nativeQuery = true)
      Integer getRemoteDataAcquisitionToleranceTotal(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("priority") String priority);

      static final String remoteDataAcquisitionAffectsTolerance = "select count(distinct(ms.id)) from sla_meter_staging ms, "
                  +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2)" +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionAffectsTolerance, nativeQuery = true)
      Integer getRemoteDataAcquisitionAffectsTolerance(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("px") String px);

      static final String remoteDataAcquisitionAffectsOverallTolerance = "select count(distinct(ms.id)) from sla_meter_staging ms, "
                  +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2)" +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionAffectsOverallTolerance, nativeQuery = true)
      Integer getRemoteDataAcquisitionAffectsOverallTolerance(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionOverallTolerance = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionOverallTolerance, nativeQuery = true)
      Integer getRemoteDataAcquisitionOverallTolerance(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("status") String status, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionTotalOverallTolerance = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionTotalOverallTolerance, nativeQuery = true)
      Integer getRemoteDataAcquisitionTotalOverallTolerance(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionOverall, nativeQuery = true)
      Integer getCommandCountOverall(@Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("status") String status, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionT = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :priority " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionT, nativeQuery = true)
      Integer getTotalCommand(@Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String remoteDataAcquisitionTOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionTOverall, nativeQuery = true)
      Integer getTotalCommandOverall(@Param("c1") String c1,
                  @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionTOverall2 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionTOverall2, nativeQuery = true)
      Integer getTotalCommandOverall2(@Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String remoteDataAcquisitionAffects = "select count(distinct(ms.id)) from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionAffects, nativeQuery = true)
      Integer getRemoteDataAcquisitionAffects(@Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("date") String date, @Param("px") String px);

      static final String remoteDataAcquisitionAffectsOverall = "select count(distinct(ms.id)) from sla_meter_staging ms, "
                  +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = remoteDataAcquisitionAffectsOverall, nativeQuery = true)
      Integer getRemoteDataAcquisitionAffectsOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String alarmInDeviceMetersP1 = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = alarmInDeviceMetersP1, nativeQuery = true)
      Integer getAlarmInDeviceMetersP1(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String alarmInDeviceMetersP2 = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = alarmInDeviceMetersP2, nativeQuery = true)
      Integer getAlarmInDeviceMetersP2(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String alarmInDeviceMetersP3 = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = alarmInDeviceMetersP3, nativeQuery = true)
      Integer getAlarmInDeviceMetersP3(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String AlarmInDeviceMetersOverall = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization;";

      @Query(value = AlarmInDeviceMetersOverall, nativeQuery = true)
      Integer getAlarmInDeviceMetersOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha,
                  @Param("prioridad1") String prioridad1, @Param("prioridad2") String prioridad2,
                  @Param("prioridad3") String prioridad3,
                  @Param("time_limit") Integer time_limit);

      static final String AlarmInDeviceTotal = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority)" +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmInDeviceTotal, nativeQuery = true)
      Integer getAlarmInDeviceTotal(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date, @Param("priority") String priority);

      static final String AlarmInDeviceTotalOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3)" +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmInDeviceTotalOverall, nativeQuery = true)
      Integer getAlarmInDeviceTotalOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String AlarmInDeviceAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px)" +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmInDeviceAffected, nativeQuery = true)
      Integer getAlarmInDeviceAffected(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date, @Param("px") String px);

      static final String AlarmInDeviceAffectOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3)" +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmInDeviceAffectOverall, nativeQuery = true)
      Integer getAlarmInDeviceAffectOverall(@Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4);

      static final String MeasurementOfPowerIndicatorsMeters = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsMeters, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsMeters(@Param("c1") String c1,
                  @Param("c2") String c2, @Param("date") String date,
                  @Param("status") String status, @Param("priority") String priority);

      static final String MeasurementOfPowerIndicatorsMetersOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsMetersOverall, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsMetersOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("status") String status,
                  @Param("p1") String p1, @Param("p2") String p2, @Param("p3") String p3);

      static final String MeasurementOfPowerIndicatorsTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsTotal, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsTotal(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("priority") String priority);

      static final String MeasurementOfPowerIndicatorsTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsTotalOverall, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsTotalOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String MeasurementOfPowerIndicatorsAffects = "select count(distinct(ms.id)) from sla_meter_staging ms, "
                  +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsAffects, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsAffects(@Param("c1") String c1,
                  @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("p1") String p1);

      static final String MeasurementOfPowerIndicatorsAffectsOverall = "select count(distinct(ms.id)) from sla_meter_staging ms, "
                  +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeasurementOfPowerIndicatorsAffectsOverall, nativeQuery = true)
      Integer getMeasurementOfPowerIndicatorsAffectsOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String RemotelyReadEventLogsPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsPx, nativeQuery = true)
      Integer getRemotelyReadEventLogsPx(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String RemotelyReadEventLogsOverall = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsOverall, nativeQuery = true)
      Integer getRemotelyReadEventLogsOverall(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad1") String prioridad1,
                  @Param("prioridad2") String prioridad2, @Param("prioridad3") String prioridad3,
                  @Param("time_limit") Integer time_limit);

      static final String RemotelyReadEventLogsTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsTotal, nativeQuery = true)
      Integer getRemotelyReadEventLogsTotal(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemotelyReadEventLogsTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsTotalOverall, nativeQuery = true)
      Integer getRemotelyReadEventLogsTotalOverall(@Param("c1") String c1,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String RemotelyReadEventLogsAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsAffected, nativeQuery = true)
      Integer getRemotelyReadEventLogsAffected(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String RemotelyReadEventLogsAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyReadEventLogsAffectedOverall, nativeQuery = true)
      Integer getRemotelyReadEventLogsAffectedOverall(@Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("c1") String c1);

      static final String RemotelyAlterSettingsSuceedMeterPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsSuceedMeterPx, nativeQuery = true)
      Integer getRemotelyAlterSettingsSuceedMeterPx(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String MaxTransDelaySwitchCommandsSuceedMeterPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, :c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, :c40, :c41) "
                  +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelaySwitchCommandsSuceedMeterPx, nativeQuery = true)
      Integer getMaxTransDelaySwitchCommandsSuceedMeterPx(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5,
                  @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8, @Param("c9") String c9,
                  @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16, @Param("c17") String c17, @Param("c18") String c18,
                  @Param("c19") String c19, @Param("c20") String c20,
                  @Param("c21") String c21, @Param("c22") String c22, @Param("c23") String c23,
                  @Param("c24") String c24, @Param("c25") String c25,
                  @Param("c26") String c26, @Param("c27") String c27, @Param("c28") String c28,
                  @Param("c29") String c29, @Param("c30") String c30,
                  @Param("c31") String c31, @Param("c32") String c32, @Param("c33") String c33,
                  @Param("c34") String c34, @Param("c35") String c35,
                  @Param("c36") String c36, @Param("c37") String c37, @Param("c38") String c38,
                  @Param("c39") String c39, @Param("c40") String c40,
                  @Param("c41") String c41,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String MaxTransDelayUpdatesSuceedMeterPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelayUpdatesSuceedMeterPx, nativeQuery = true)
      Integer getMaxTransDelayUpdatesSuceedMeterPx(
                  @Param("c1") String c1,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String RemotelyAlterSettingsSuceedMeterOverall = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsSuceedMeterOverall, nativeQuery = true)
      Integer getRemotelyAlterSettingsSuceedMeterOverall(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad1") String prioridad1,
                  @Param("prioridad2") String prioridad2, @Param("prioridad3") String prioridad3,
                  @Param("time_limit") Integer time_limit);

      static final String RemotelyAlterSettingsTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c0,:c1,:c2,:c3,:c4,:c5,:c6,:c7,:c8,:c9,:c10,:c11, :c12) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsTotal, nativeQuery = true)
      Integer getRemotelyAlterSettingsTotal(@Param("c0") String c0,
                  @Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6,
                  @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12,
                  @Param("date") String date, @Param("priority") String priority);

      static final String MaxTransDelaySwitchCommandsTotalPX = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, :c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, :c40, :c41) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelaySwitchCommandsTotalPX, nativeQuery = true)
      Integer getMaxTransDelaySwitchCommandsTotalPX(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5,
                  @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8, @Param("c9") String c9,
                  @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16, @Param("c17") String c17, @Param("c18") String c18,
                  @Param("c19") String c19, @Param("c20") String c20,
                  @Param("c21") String c21, @Param("c22") String c22, @Param("c23") String c23,
                  @Param("c24") String c24, @Param("c25") String c25,
                  @Param("c26") String c26, @Param("c27") String c27, @Param("c28") String c28,
                  @Param("c29") String c29, @Param("c30") String c30,
                  @Param("c31") String c31, @Param("c32") String c32, @Param("c33") String c33,
                  @Param("c34") String c34, @Param("c35") String c35,
                  @Param("c36") String c36, @Param("c37") String c37, @Param("c38") String c38,
                  @Param("c39") String c39, @Param("c40") String c40,
                  @Param("c41") String c41,
                  @Param("date") String date, @Param("priority") String priority);

      static final String MaxTransDelayUpdatesTotalPX = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelayUpdatesTotalPX, nativeQuery = true)
      Integer getMaxTransDelayUpdatesTotalPX(
                  @Param("c1") String c1,
                  @Param("date") String date, @Param("priority") String priority);

      static final String RemotelyAlterSettingsTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c0,:c1,:c2,:c3,:c4,:c5,:c6,:c7,:c8,:c9,:c10,:c11, :c12) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsTotalOverall, nativeQuery = true)
      Integer getRemotelyAlterSettingsTotalOverall(@Param("c0") String c0,
                  @Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6,
                  @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemotelyAlterSettingsAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c0,:c1,:c2,:c3,:c4,:c5,:c6,:c7,:c8,:c9,:c10,:c11, :c12) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsAffected, nativeQuery = true)
      Integer getRemotelyAlterSettingsAffected(@Param("c0") String c0,
                  @Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6,
                  @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String MaxTransDelaySwitchCommandsAffectedPx = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, :c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, :c40, :c41) "
                  +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelaySwitchCommandsAffectedPx, nativeQuery = true)
      Integer getMaxTransDelaySwitchCommandsAffectedPx(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5,
                  @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8, @Param("c9") String c9,
                  @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16, @Param("c17") String c17, @Param("c18") String c18,
                  @Param("c19") String c19, @Param("c20") String c20,
                  @Param("c21") String c21, @Param("c22") String c22, @Param("c23") String c23,
                  @Param("c24") String c24, @Param("c25") String c25,
                  @Param("c26") String c26, @Param("c27") String c27, @Param("c28") String c28,
                  @Param("c29") String c29, @Param("c30") String c30,
                  @Param("c31") String c31, @Param("c32") String c32, @Param("c33") String c33,
                  @Param("c34") String c34, @Param("c35") String c35,
                  @Param("c36") String c36, @Param("c37") String c37, @Param("c38") String c38,
                  @Param("c39") String c39, @Param("c40") String c40,
                  @Param("c41") String c41,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String MaxTransDelayUpdatesAffectedPx = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = MaxTransDelayUpdatesAffectedPx, nativeQuery = true)
      Integer getMaxTransDelayUpdatesAffectedPx(
                  @Param("c1") String c1,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String RemotelyAlterSettingsAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c0,:c1,:c2,:c3,:c4,:c5,:c6,:c7,:c8,:c9,:c10,:c11, :c12) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemotelyAlterSettingsAffectedOverall, nativeQuery = true)
      Integer getRemotelyAlterSettingsAffectedOverall(@Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("c0") String c0,
                  @Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6,
                  @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12);

      static final String MeterLossOfSupplySuceedMeterPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2, :comando3, :comando4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplySuceedMeterPx, nativeQuery = true)
      Integer getMeterLossOfSupplySuceedMeterPx(@Param("comando1") String comando1, @Param("comando2") String comando2,
                  @Param("comando3") String comando3, @Param("comando4") String comando4,
                  @Param("fecha") String fecha, @Param("estado") String estado,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String MeterLossOfSupplySuceedMeterOverall = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2, :comando3, :comando4) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplySuceedMeterOverall, nativeQuery = true)
      Integer getMeterLossOfSupplySuceedMeterOverall(@Param("comando1") String comando1,
                  @Param("comando2") String comando2,
                  @Param("comando3") String comando3, @Param("comando4") String comando4,
                  @Param("fecha") String fecha, @Param("estado") String estado,
                  @Param("prioridad1") String prioridad1, @Param("prioridad2") String prioridad2,
                  @Param("prioridad3") String prioridad3, @Param("time_limit") Integer time_limit);

      static final String MeterLossOfSupplyTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :priority " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplyTotal, nativeQuery = true)
      Integer getMeterLossOfSupplyTotal(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String MeterLossOfSupplyTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplyTotalOverall, nativeQuery = true)
      Integer getMeterLossOfSupplyTotalOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String MeterLossOfSupplyAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :px " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplyAffected, nativeQuery = true)
      Integer getMeterLossOfSupplyAffected(@Param("c1") String c1,
                  @Param("c2") String c2,
                  @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String MeterLossOfSupplyAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterLossOfSupplyAffectedOverall, nativeQuery = true)
      Integer getMeterLossOfSupplyAffectedOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("date") String date, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String RemoteConnectDisconnectForSelectedConsumersPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersPx, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersPx(@Param("comando1") String comando1,
                  @Param("comando2") String comando2,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String RemoteConnectDisconnectForSelectedConsumersOverall = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersOverall, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersOverall(@Param("comando1") String comando1,
                  @Param("comando2") String comando2,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad1") String prioridad1,
                  @Param("prioridad2") String prioridad2, @Param("prioridad3") String prioridad3,
                  @Param("time_limit") Integer time_limit);

      static final String RemoteConnectDisconnectForSelectedConsumersTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersTotal, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersTotal(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemoteConnectDisconnectForSelectedConsumersTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersTotalOverall, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersTotalOverall(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteConnectDisconnectForSelectedConsumersAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersAffected, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersAffected(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String RemoteConnectDisconnectForSelectedConsumersAffectedOverall = "select count(distinct(ms.id)) "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteConnectDisconnectForSelectedConsumersAffectedOverall, nativeQuery = true)
      Integer getRemoteConnectDisconnectForSelectedConsumersAffectedOverall(@Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("c1") String c1, @Param("c2") String c2);

      static final String RemoteLoadControlCommandsPx = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority in (:prioridad) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsPx, nativeQuery = true)
      Integer getRemoteLoadControlCommandsPx(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("time_limit") Integer time_limit,
                  @Param("prioridad") String prioridad);

      static final String RemoteLoadControlCommandsP1 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p1' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '15' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '24' * '60' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) + " +
                  "date_part('seconds', cs.finishtime - cs.inittime) / '60') < mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsP1, nativeQuery = true)
      Integer getRemoteLoadControlCommandsP1(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String RemoteLoadControlCommandsP2 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p2' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '15' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '24' * '60' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) + " +
                  "date_part('seconds', cs.finishtime - cs.inittime) / '60') < mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsP2, nativeQuery = true)
      Integer getRemoteLoadControlCommandsP2(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String RemoteLoadControlCommandsP3 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p3' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '15' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '24' * '60' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) + " +
                  "date_part('seconds', cs.finishtime - cs.inittime) / '60') < mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsP3, nativeQuery = true)
      Integer getRemoteLoadControlCommandsP3(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String RemoteLoadControlCommandsOverall = "select count (*) from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and mo.type = 'Overall' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '15' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '24' * '60' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) + " +
                  "date_part('seconds', cs.finishtime - cs.inittime) / '60') < mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsOverall, nativeQuery = true)
      Integer getRemoteLoadControlCommandsOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String RemoteLoadControlCommandsPertaining = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsPertaining, nativeQuery = true)
      Integer getRemoteLoadControlCommandsPertaining(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String RemoteLoadControlCommandsOverallNew = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 < :time_limit " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsOverallNew, nativeQuery = true)
      Integer getRemoteLoadControlCommandsOverallNew(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("time_limit") Integer time_limit,
                  @Param("prioridad1") String prioridad1,
                  @Param("prioridad2") String prioridad2, @Param("prioridad3") String prioridad3);

      static final String RemoteLoadControlCommandsTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsTotal, nativeQuery = true)
      Integer getRemoteLoadControlCommandsTotal(@Param("c1") String c1, @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemoteLoadControlCommandsTotalNew = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsTotalNew, nativeQuery = true)
      Integer getRemoteLoadControlCommandsTotalNew(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("priority") String priority);

      static final String RemoteLoadControlCommandsTotalPx = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsTotalPx, nativeQuery = true)
      Integer getRemoteLoadControlCommandsTotalPx(@Param("c1") String c1, @Param("date") String date,
                  @Param("prioridad") String prioridad);

      static final String RemoteLoadControlCommandsTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsTotalOverall, nativeQuery = true)
      Integer getRemoteLoadControlCommandsTotalOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("prioridad") String prioridad1, @Param("prioridad") String prioridad2,
                  @Param("prioridad") String prioridad3);

      static final String RemoteLoadControlCommandsTotalOverallNew = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsTotalOverallNew, nativeQuery = true)
      Integer getRemoteLoadControlCommandsTotalOverallNew(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteLoadControlCommandsAffectedNew = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsAffectedNew, nativeQuery = true)
      Integer getRemoteLoadControlCommandsAffectedNew(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("date") String date,
                  @Param("px") String px);

      static final String RemoteLoadControlCommandsAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsAffected, nativeQuery = true)
      Integer getRemoteLoadControlCommandsAffected(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("prioridad") String prioridad);

      static final String RemoteLoadControlCommandsAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteLoadControlCommandsAffectedOverall, nativeQuery = true)
      Integer getRemoteLoadControlCommandsAffectedOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String ReconnectionsFromCustomerP1 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p1' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '16' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '86400' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '3600' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('seconds', cs.finishtime - cs.inittime)) > mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerP1, nativeQuery = true)
      Integer getReconnectionsFromCustomerP1(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String ReconnectionsFromCustomerP2 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p2' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '16' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '86400' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '3600' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('seconds', cs.finishtime - cs.inittime)) > mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerP2, nativeQuery = true)
      Integer getReconnectionsFromCustomerP2(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String ReconnectionsFromCustomerP3 = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:px) " +
                  "and mo.type = 'p3' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '16' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '86400' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '3600' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('seconds', cs.finishtime - cs.inittime)) > mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerP3, nativeQuery = true)
      Integer getReconnectionsFromCustomerP3(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String ReconnectionsFromCustomerOverall = "select count (*) from sla_meter_staging ms, " +
                  "sla_command_staging cs, " +
                  "sla_calculation_methods_objective mo, " +
                  "sla_calculation_methods cm " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :status " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and mo.type = 'Overall' " +
                  "and mo.calculation_methods_id = cm.id " +
                  "and cm.calculation_module_id = '16' " +
                  "and (date_part('day' , cs.finishtime - cs.inittime) * '86400' + " +
                  "date_part('hours', cs.finishtime - cs.inittime) * '3600' + " +
                  "date_part('minutes', cs.finishtime - cs.inittime) * '60' + " +
                  "date_part('seconds', cs.finishtime - cs.inittime)) > mo.limit_times " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerOverall, nativeQuery = true)
      Integer getReconnectionsFromCustomerOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String ReconnectionsFromCustomerTotal = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:priority) " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerTotal, nativeQuery = true)
      Integer getReconnectionsFromCustomerTotal(@Param("c1") String c1, @Param("date") String date,
                  @Param("priority") String priority);

      static final String ReconnectionsFromCustomerTotalOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerTotalOverall, nativeQuery = true)
      Integer getReconnectionsFromCustomerTotalOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String ReconnectionsFromCustomerAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = ReconnectionsFromCustomerAffected, nativeQuery = true)
      Integer getReconnectionsFromCustomerAffected(@Param("c1") String c1, @Param("date") String date,
                  @Param("px") String px);

      static final String ReconnectionsFromCustomerAffectedOverall = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = ReconnectionsFromCustomerAffectedOverall, nativeQuery = true)
      Integer getReconnectionsFromCustomerAffectedOverall(@Param("fecha") String fecha,
                  @Param("prioridad1") String prioridad1,
                  @Param("prioridad2") String prioridad2, @Param("prioridad3") String prioridad3,
                  @Param("comando") String comando,
                  @Param("time_limit") Integer time_limit);

      static final String ReconnectionsFromCustomerP1Affects = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = ReconnectionsFromCustomerP1Affects, nativeQuery = true)
      Integer getReconnectionsFromCustomerP1Affects(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String ReconnectionsFromCustomerP2Affects = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = ReconnectionsFromCustomerP2Affects, nativeQuery = true)
      Integer getReconnectionsFromCustomerP2Affects(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String ReconnectionsFromCustomerP3Affects = "select count(distinct(ms.id )) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime)) > :time_limit " +
                  "and ms.organization = cs.organization; ";

      @Query(value = ReconnectionsFromCustomerP3Affects, nativeQuery = true)
      Integer getReconnectionsFromCustomerP3Affects(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      static final String integrationOfDERMeterCount = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERMeterCount, nativeQuery = true)
      Integer getIntegrationOfDERMeterCount(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String integrationOfDERMeterCountOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERMeterCountOverall, nativeQuery = true)
      Integer getIntegrationOfDERMeterCountOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String integrationOfDERTotalMeters = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERTotalMeters, nativeQuery = true)
      Integer getIntegrationOfDERTotalMeters(@Param("c1") String c1, @Param("date") String date,
                  @Param("px") String px);

      static final String integrationOfDERTotalMetersOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERTotalMetersOverall, nativeQuery = true)
      Integer getIntegrationOfDERTotalMetersOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String integrationOfDERAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERAffected, nativeQuery = true)
      Integer getIntegrationOfDERAffected(@Param("c1") String c1, @Param("date") String date, @Param("px") String px);

      static final String integrationOfDERAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = integrationOfDERAffectedOverall, nativeQuery = true)
      Integer getIntegrationOfDERAffectedOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteApplianceControlOnDemand = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemand, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemand(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String RemoteApplianceControlOnDemandOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemandOverall, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemandOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteApplianceControlOnDemandTotalMeters = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemandTotalMeters, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemandTotalMeters(@Param("c1") String c1, @Param("date") String date,
                  @Param("px") String px);

      static final String RemoteApplianceControlOnDemandTotalMetersOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemandTotalMetersOverall, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemandTotalMetersOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteApplianceControlOnDemandAffected = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemandAffected, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemandAffected(@Param("c1") String c1, @Param("date") String date,
                  @Param("px") String px);

      static final String RemoteApplianceControlOnDemandAffectedOverall = "select count(distinct(ms.id)) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteApplianceControlOnDemandAffectedOverall, nativeQuery = true)
      Integer getRemoteApplianceControlOnDemandAffectedOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownMetersCount = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownMetersCount, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownMetersCount(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownOverall, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownOverall(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownTotalMeters = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
				  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownTotalMeters, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownTotalMeters(@Param("c1") String c1,
                  @Param("date") String date, @Param("px") String px);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownTotalMetersOverall = "select count(*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownTotalMetersOverall, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownTotalMetersOverall(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownAffected = "select count(distinct(ms.id)) "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownAffected, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownAffected(@Param("c1") String c1,
                  @Param("date") String date, @Param("px") String px);

      static final String RemoteControlOfDecentralizedGenerationUpToShutDownAffectedOverall = "select count(distinct(ms.id)) "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = RemoteControlOfDecentralizedGenerationUpToShutDownAffectedOverall, nativeQuery = true)
      Integer getRemoteControlOfDecentralizedGenerationUpToShutDownAffectedOverall(@Param("c1") String c1,
                  @Param("date") String date,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3);

      static final String MeterAlarmsLoggedEventsMeterCount = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterAlarmsLoggedEventsMeterCount, nativeQuery = true)
      Integer getMeterAlarmsLoggedEventsMeterCount(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String MeterAlarmsLoggedEventsMeterCountOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = MeterAlarmsLoggedEventsMeterCountOverall, nativeQuery = true)
      Integer getMeterAlarmsLoggedEventsMeterCountOverall(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3);

      static final String AlarmsloggedeventsTotalMetersOverall = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmsloggedeventsTotalMetersOverall, nativeQuery = true)
      Integer getAlarmsloggedeventsTotalMetersOverall(@Param("c1") String c1,
                  @Param("p1") String p1,
                  @Param("p2") String p2,
                  @Param("p3") String p3,
                  @Param("date") String date);

      static final String AlarmsloggedeventsTotalMeters = "select count (*) " +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.organization = cs.organization";

      @Query(value = AlarmsloggedeventsTotalMeters, nativeQuery = true)
      Integer getAlarmsloggedeventsTotalMeters(@Param("c1") String c1,
                  @Param("px") String px,
                  @Param("date") String date);

      static final String WaterCountP = "select sum(f.tot) " +
                  "from (select lp.process_date, lp.priority, lp.serial_number, " +
                  "(CASE WHEN (lp.count > 1) THEN '1' " +
                  "ELSE " +
                  "lp.count END) as tot " +
                  "from (select CAST(ms.process_date AS Date) as process_date, ms.priority, ms.serial_number, count(*) "
                  +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = CAST(:date AS Timestamp) " +
				  "and cs.datetime >=  cast(:date AS Timestamp) " +
				  "and cs.datetime <  (cast(:date AS Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and cs.order_status in (:status) " +
                  "and cs.order_name in (:c1) " +
                  "group by CAST(ms.process_date AS Date), ms.priority, ms.serial_number " +
                  "order by 1,2) lp) f";

      @Query(value = WaterCountP, nativeQuery = true)
      Integer getWaterCountP(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String WaterEnergyCountPnewSL17 = "select ms.serial_number, count(*) " +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and cs.order_status = :estado " +
                  "and ms.organization = cs.organization " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE', 'DISCONNECTED') " +
                  "and cs.order_name = :comando " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :limit_time " +
                  "group by ms.serial_number " +
                  "having count(*) >= :cantidad";

      @Query(value = WaterEnergyCountPnewSL17, nativeQuery = true)
      List<Object[]> getWaterEnergyCountPnewSL17(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("limit_time") Integer limit_time,
                  @Param("cantidad") Integer cantidad, @Param("estado") String estado,
                  @Param("prioridad") String prioridad);

      static final String WaterEnergyCountPnewSL = "select  count(distinct(ms.serial_number)) " +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and cs.order_status = :estado " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and ms.organization = cs.organization " +
                  "and cs.order_name = :comando " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :limit_time " +
                  "and ms.meter_type = :tipo "+
                  "and :cantidad=:cantidad";
                  ;
                 // "group by ms.serial_number " +
                 // "having count(*) >= :cantidad"
                 
      @Query(value = WaterEnergyCountPnewSL, nativeQuery = true)
      
      Integer getWaterEnergyCountPnewSL(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("limit_time") Integer limit_time,
                  @Param("cantidad") Integer cantidad,
                  @Param("estado") String estado,
                  @Param("prioridad") String prioridad, @Param("tipo") String tipo);

      static final String WaterEnergyCountPnew = "select ms.serial_number, count('x') " +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and cs.order_status = :estado " +
                  "and ms.organization = cs.organization " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE', 'DISCONNECTED') " +
                  "and cs.order_name = :comando " +
                  "and ms.meter_type = :tipo " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :limit_time " +
                  "group by ms.serial_number " +
                  "having count(*) >= :cantidad";

      @Query(value = WaterEnergyCountPnew, nativeQuery = true)
      List<Object[]>  getWaterEnergyCountPnew(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("tipo") String tipo, @Param("limit_time") Integer limit_time,
                  @Param("cantidad") Integer cantidad, @Param("estado") String estado,
                  @Param("prioridad") String prioridad);

      static final String EnergyRegisterCount = "select sum(f.tot) " +
                  "from (select lp.process_date, lp.priority, lp.serial_number, " +
                  "(CASE WHEN (lp.count > 1) THEN '1' " +
                  "ELSE " +
                  "lp.count END) as tot " +
                  "from (select CAST(ms.process_date AS Date) as process_date, ms.priority, ms.serial_number, count(*) "
                  +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = CAST(:date as Timestamp) " +
				  "and cs.datetime >=  cast(:date as Timestamp) " +
				  "and cs.datetime <  (cast(:date  as Timestamp)+ interval '1 day') " +
                  "and ms.priority in (:px) " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and cs.order_status in (:status) " +
                  "and cs.order_name in (:c1) " +
                  "group by CAST(ms.process_date AS Date), ms.priority, ms.serial_number " +
                  "order by 1,2) lp) f ";

      @Query(value = EnergyRegisterCount, nativeQuery = true)
      Integer getEnergyRegisterCountP(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px);

      static final String EnergyAffectsCount = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date as Timestamp)" +
                  "and ms.meter_type='ELECTRICITY'";

      @Query(value = EnergyAffectsCount, nativeQuery = true)
      Integer getEnergyAffectsCount(@Param("date") String date, @Param("px") String px);

      static final String waterAffectsCount = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and ms.priority in (:px) " +
                  "and ms.process_date=CAST(:date as Timestamp)" +
                  "and ms.meter_type='WATER'";

      @Query(value = waterAffectsCount, nativeQuery = true)
      Integer getWaterAffectsCount(@Param("date") String date, @Param("px") String px);

      static final String IndividualReadHES = "select count(*) " +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = CAST(:date as Timestamp) " +
				  "and cs.datetime >=  cast(:date as Timestamp) " +
				  "and cs.datetime <  (cast(:date  as Timestamp)+ interval '1 day') " +
                  "and cs.order_status in (:status) " +
                  "and ms.priority in (:px) " +
                  "and cs.finishtime <= cast(:timestp as timestamp)";

      @Query(value = IndividualReadHES, nativeQuery = true)
      Integer getIndividualReadHES(@Param("c1") String c1, @Param("date") String date,
                  @Param("status") String status, @Param("px") String px,
                  @Param("timestp") String timestamp);

      static final String SpatialAvailabilityHesZone = "select distinct(zone) " +
                  "from sla_meter_staging where process_date = CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesZone, nativeQuery = true)
      List<String> getSpatialAvailabilityHesZone(@Param("date") String date);

      static final String SpatialAvailabilityHesActive = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2) " +
                  "and ms.priority in (:px) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date= CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesActive, nativeQuery = true)
      Integer getSpatialAvailabilityHesActive(@Param("status") String status,
                  @Param("status2") String status2,
                  @Param("px") String px,
                  @Param("zone") String zone,
                  @Param("date") String date);

      static final String SpatialAvailabilityHesBaseline = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:px) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesBaseline, nativeQuery = true)
      Integer getSpatialAvailabilityHesBaseline(@Param("param1") String param1, @Param("param2") String param2,
                  @Param("param3") String param3, @Param("px") String px,
                  @Param("zone") String zone, @Param("date") String date);

      static final String SpatialAvailabilityHesActiveOverall = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:status, :status2) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date = CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesActiveOverall, nativeQuery = true)
      Integer getSpatialAvailabilityHesActiveOverall(@Param("status") String status, @Param("status2") String status2,
                  @Param("p1") String p1, @Param("p2") String p2,
                  @Param("p3") String p3, @Param("zone") String zone,
                  @Param("date") String date);

      static final String SpatialAvailabilityHesBaselineOverall = "select count (*) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesBaselineOverall, nativeQuery = true)
      Integer getSpatialAvailabilityHesBaselineOverall(@Param("param1") String param1, @Param("param2") String param2,
                  @Param("param3") String param3, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("zone") String zone, @Param("date") String date);

      static final String SpatialAvailabilityHesAffects = "select count(distinct(ms.id)) from sla_meter_staging ms " +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:px) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesAffects, nativeQuery = true)
      Integer getSpatialAvailabilityHesAffects(@Param("param1") String param1, @Param("param2") String param2,
                  @Param("param3") String param3, @Param("px") String px,
                  @Param("zone") String zone, @Param("date") String date);

      static final String SpatialAvailabilityHesAffectsOverall = "select count(distinct(ms.id)) from sla_meter_staging ms "
                  +
                  "where ms.status in (:param1, :param2, :param3) " +
                  "and ms.priority in (:p1, :p2, :p3) " +
                  "and ms.zone = (:zone) " +
                  "and ms.process_date=CAST(:date as Timestamp)";

      @Query(value = SpatialAvailabilityHesAffectsOverall, nativeQuery = true)
      Integer getSpatialAvailabilityHesAffectsOverall(@Param("param1") String param1, @Param("param2") String param2,
                  @Param("param3") String param3, @Param("p1") String p1,
                  @Param("p2") String p2, @Param("p3") String p3,
                  @Param("zone") String zone, @Param("date") String date);

      static final String ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance = "select AVG(EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))) "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comandos) " +
                  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha  as Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and ms.organization = cs.organization;";

      @Query(value = ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance, nativeQuery = true)
      Float getReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(@Param("fecha") String fecha,
                  @Param("prioridad") String prioridad, @Param("comandos") String comandos,
                  @Param("estado") String estado);

      static final String ReconnectionsFromCustomerCareCenterToEnergySupplyToleranceOverall = "select AVG(EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))) "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comandos) " +
                  /*
                  "and CAST(cs.datetime As Date) = CAST(:fecha AS Timestamp) " +
                  "and CAST(cs.datetime As Date) = CAST(ms.process_date AS Date) " +
                   */
				  "and ms.process_date = CAST(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha  as Timestamp)+ interval '1 day') " +
                  
                  "and cs.order_status = :estado " +
                  "and ms.priority in (:prioridad1, :prioridad2, :prioridad3) " +
                  "and ms.organization = cs.organization;";

      @Query(value = ReconnectionsFromCustomerCareCenterToEnergySupplyToleranceOverall, nativeQuery = true)
      Float getReconnectionsFromCustomerCareCenterToEnergySupplyToleranceOverall(@Param("fecha") String fecha,
                  @Param("prioridad1") String prioridad1, @Param("prioridad2") String prioridad2,
                  @Param("prioridad3") String prioridad3,
                  @Param("comandos") String comandos, @Param("estado") String estado);

      static final String mappedCommand = "SELECT id, priority, serial_number, zone, process_date, organization, status "
                  +
                  "FROM sla_meter_staging sm " +
                  "where sm.serial_number = :nro";

      @Query(value = mappedCommand, nativeQuery = true)
      List<SLADataMeter> getCommandBy(@Param("nro") String nro);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = 'OnDemandSetTariffAgreement' " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != 'FINISH_OK' " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedConfigurationofMeterforTimeofUse(@Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, null as order_name, null as order_status, null as inittime, null as finishtime "
                  +
                  "from sla_meter_staging ms " +
                  "where ms.status in ('UNREACHABLE') " +
                  "and ms.process_date  = CAST(:fecha AS Timestamp)", nativeQuery = true)
      List<Object[]> getFailedNetworkAvailability(@Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedConfigurationOfMeterForPrepaid(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = 'OnDemandSetLoadLimitation' " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != 'FINISH_OK' " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteLoadControl(@Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedAutomaticServiceReductionInterruption(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteConnectDisconnect(@Param("c1") String c1, @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18) "
                  +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteDataAcquisition(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3,
                  @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10, @Param("c11") String c11, @Param("c12") String c12,
                  @Param("c13") String c13, @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16,
                  @Param("c17") String c17, @Param("c18") String c18, @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMeasurementOfPowerIndicators(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, null as order_name, null as order_status, null as inittime, null as finishtime "
                  +
                  "from sla_meter_staging ms " +
                  "where ms.status in ('UNREACHABLE') " +
                  "and  ms.process_date = CAST(:fecha AS Timestamp)", nativeQuery = true)
      List<Object[]> getFailedNetworkavailibityHES(@Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in ('OnDemandEventLog') " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != 'FINISH_OK'" +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMeterAlarmsLoggedEvents(@Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, null as order_name, null as order_status, null as inittime, null as finishtime "
                  +
                  "from sla_meter_staging ms " +
                  "where ms.status in ('UNREACHABLE') " +
                  "and ms.zone in (:zone) " +
                  "and ms.process_date ) = CAST(:fecha AS Timestamp)", nativeQuery = true)
      List<Object[]> getFailedSpatialAvailabilityHES(@Param("zone") String zone,
                  @Param("fecha") String fecha);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != 'FINISH_OK' " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteDataAcquisitionTolerance(@Param("fecha") String fecha,
                  @Param("comando1") String comando1, @Param("comando2") String comando2);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 < :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterA(
                  @Param("comando") String comando,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterB(
                  @Param("comando") String comando,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeA(@Param("c1") String c1,
                  @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, :c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, :c40, :c41) "
                  +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMaxTransDelaySwitchCommandsA(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5,
                  @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8, @Param("c9") String c9,
                  @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16, @Param("c17") String c17, @Param("c18") String c18,
                  @Param("c19") String c19, @Param("c20") String c20,
                  @Param("c21") String c21, @Param("c22") String c22, @Param("c23") String c23,
                  @Param("c24") String c24, @Param("c25") String c25,
                  @Param("c26") String c26, @Param("c27") String c27, @Param("c28") String c28,
                  @Param("c29") String c29, @Param("c30") String c30,
                  @Param("c31") String c31, @Param("c32") String c32, @Param("c33") String c33,
                  @Param("c34") String c34, @Param("c35") String c35,
                  @Param("c36") String c36, @Param("c37") String c37, @Param("c38") String c38,
                  @Param("c39") String c39, @Param("c40") String c40,
                  @Param("c41") String c41,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMaxTransDelayUpdatesA(@Param("c1") String c1,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeB(@Param("c1") String c1,
                  @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5, @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8,
                  @Param("c9") String c9, @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4, :c5, :c6, :c7, :c8, :c9, :c10, :c11, :c12, :c13, :c14, :c15, :c16, :c17, :c18, :c19, :c20, :c21, :c22, :c23, :c24, :c25, :c26, :c27, :c28, :c29, :c30, :c31, :c32, :c33, :c34, :c35, :c36, :c37, :c38, :c39, :c40, :c41) "
                  +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMaxTransDelaySwitchCommandsB(
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4,
                  @Param("c5") String c5,
                  @Param("c6") String c6, @Param("c7") String c7, @Param("c8") String c8, @Param("c9") String c9,
                  @Param("c10") String c10,
                  @Param("c11") String c11, @Param("c12") String c12, @Param("c13") String c13,
                  @Param("c14") String c14, @Param("c15") String c15,
                  @Param("c16") String c16, @Param("c17") String c17, @Param("c18") String c18,
                  @Param("c19") String c19, @Param("c20") String c20,
                  @Param("c21") String c21, @Param("c22") String c22, @Param("c23") String c23,
                  @Param("c24") String c24, @Param("c25") String c25,
                  @Param("c26") String c26, @Param("c27") String c27, @Param("c28") String c28,
                  @Param("c29") String c29, @Param("c30") String c30,
                  @Param("c31") String c31, @Param("c32") String c32, @Param("c33") String c33,
                  @Param("c34") String c34, @Param("c35") String c35,
                  @Param("c36") String c36, @Param("c37") String c37, @Param("c38") String c38,
                  @Param("c39") String c39, @Param("c40") String c40,
                  @Param("c41") String c41,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMaxTransDelayUpdatesB(@Param("c1") String c1,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMeterLossOfSupplyAndOutageDetectionA(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedMeterLossOfSupplyAndOutageDetectionB(@Param("c1") String c1, @Param("c2") String c2,
                  @Param("c3") String c3, @Param("c4") String c4,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteConnectDisconnectForSelectedConsumersA(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteConnectDisconnectForSelectedConsumersB(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedReadTheEventLogsPertainingToAllMetersA(@Param("comando") String comando,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedReadTheEventLogsPertainingToAllMetersB(@Param("comando") String comando,
                  @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:c1, :c2, :c3, :c4) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedAlarmInDeviceUntilMessageReachesCustomer(@Param("fecha") String fecha,
                  @Param("c1") String c1, @Param("c2") String c2, @Param("c3") String c3, @Param("c4") String c4);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != 'FINISH_OK' " +
                  "and cs.order_name in ('EnergyProfile','WaterProfile')", nativeQuery = true)
      List<Object[]> getFailedCollectionOfDailyMeterIntervalReadingHES1(@Param("fecha") String date);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, null as order_name, null as order_status, null as inittime, null as finishtime "
                  +
                  "from sla_meter_staging ms " +
                  "where ms.process_date = cast(:date as Timestamp) and not exists(" +
                  "select 1 from sla_command_staging cs " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = cast(:date as Timestamp) " +
				  "and cs.datetime >=  cast(:date as Timestamp) " +
				  "and cs.datetime <  (cast(:date as Timestamp)+ interval '1 day') " +
                  "and cs.order_name in ('EnergyProfile'))", nativeQuery = true)
      List<Object[]> getFailedCollectionOfDailyMeterIntervalReadingHES2(@Param("date") String date);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, null as order_name, null as order_status, null as inittime, null as finishtime "
                  +
                  "from sla_meter_staging ms " +
                  "where ms.process_date = cast(:date as Timestamp) and not exists(" +
                  "select 1 from sla_command_staging cs " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = cast(:date as Timestamp) " +
				  "and cs.datetime >=  cast(:date as Timestamp) " +
				  "and cs.datetime <  (cast(:date as Timestamp)+ interval '1 day') " +
                  "and cs.order_name in ('WaterProfile', 'EnergyProfile'))", nativeQuery = true)
      List<Object[]> getFailedCollectionOfDailyMeterIntervalReadingHES3(@Param("date") String date);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and cs.order_status = :estado " +
                  "and ms.organization = cs.organization " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and cs.order_name = :comando " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 >= :limit_time", nativeQuery = true)
      List<Object[]> getFailedIndividualReadHESA(@Param("comando") String comando,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("limit_time") Integer limit_time);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in  (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedIndividualReadHESB(@Param("comando") String comando, @Param("fecha") String fecha,
                  @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_command_staging cs, sla_meter_staging ms " +
                  "where ms.serial_number = cs.device_name " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.priority = :prioridad " +
                  "and cs.order_status = :estado " +
                  "and ms.organization = cs.organization " +
                  "and ms.status in ('ACTIVE', 'UNREACHABLE','DISCONNECTED') " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/3600 >= :time_limit", nativeQuery = true)
      List<Object[]> getFailedCollectionOfDailyMeterIntervalReadingToleranceA(@Param("comando1") String comando1,
                  @Param("comando2") String comando2,
                  @Param("fecha") String fecha, @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedCollectionOfDailyMeterIntervalReadingToleranceB(@Param("comando1") String comando1,
                  @Param("comando2") String comando2,
                  @Param("fecha") String fecha, @Param("estado") String estado);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(
                  @Param("fecha") String fecha, @Param("comando") String comando);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name = :comando " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedReconnectionsFromCustomerCareCenterToEnergySuppy(@Param("fecha") String fecha,
                  @Param("comando") String comando);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and ms.status = :estado " +
                  "and ms.priority = :prioridad " +
                  "and EXTRACT(EPOCH FROM (cs.finishtime - cs.inittime))/60 >= :time_limit " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteLoadControlCommandsA(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado, @Param("prioridad") String prioridad,
                  @Param("time_limit") Integer time_limit);

      @Query(value = "select ms.serial_number, ms.status, ms.zone, ms.organization, ms.priority, cs.order_name, cs.order_status, cs.inittime, cs.finishtime "
                  +
                  "from sla_meter_staging ms, " +
                  "sla_command_staging cs " +
                  "where cs.device_name = ms.serial_number " +
                  "and cs.order_name in  (:comando1, :comando2) " +
                  "and ms.process_date = cast(:fecha AS Timestamp) " +
				  "and cs.datetime >=  cast(:fecha AS Timestamp) " +
				  "and cs.datetime <  (cast(:fecha AS Timestamp)+ interval '1 day') " +
                  "and cs.order_status != :estado " +
                  "and ms.organization = cs.organization", nativeQuery = true)
      List<Object[]> getFailedRemoteLoadControlCommandsB(@Param("comando1") String comando1,
                  @Param("comando2") String comando2, @Param("fecha") String fecha,
                  @Param("estado") String estado);

}