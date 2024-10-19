package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLADailyConsolidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public interface SLADailyConsolidationRepository extends JpaRepository<SLADailyConsolidation, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sla_daily_consolidation sd " +
            "WHERE EXTRACT(MONTH FROM sd.sla_date) = :mes AND EXTRACT(YEAR FROM sd.sla_date) = :anio", nativeQuery = true)
    void deleteSlaDateConsolidation(@Param("anio") Integer anio, @Param("mes") Integer mes);

    static final String SlaDateAndSlaName = "select * " +
            "from " +
            "sla_daily_consolidation sd " +
            "where sd.sla_name = :slaName " +
            "and sd.sla_date = CAST(:slaDate as Date) " +
            "and (:zone IS NULL OR sd.zone = CAST(:zone AS character varying))";

    @Query(value = SlaDateAndSlaName, nativeQuery = true)
    SLADailyConsolidation findBySlaDateAndSlaNameAndZone(@Param("slaDate") String slaDate,
            @Param("slaName") String slaName, @Param("zone") String zone);

    static final String details = "select " +
            "count(*) " +
            "from " +
            "sla_daily_consolidation sd " +
            "where sd.sla_name = :slaName " +
            "and sd.sla_date = CAST(:slaDate as Date) " +
            "and (:zone IS NULL OR sd.zone = CAST(:zone AS character varying))";

    @Query(value = details, nativeQuery = true)
    Integer getDetails(@Param("slaDate") String slaDate, @Param("slaName") String slaName,
            @Param("zone") String zone);

    static final String numberOfTimesNotsucceededP1 = " select date_part('Month', sla_date), count(*) " +
            "from sla_daily_consolidation " +
            "where sla_name = :sla " +
            "and date_part('Year', sla_date) = :year " +
            "and p1_success = 'false' " +
            "group by date_part('Month', sla_date) " +
            "having count(*) > :number_of_times";

    @Query(value = numberOfTimesNotsucceededP1, nativeQuery = true)
    ArrayList<String> getNumberOfTimesNotsucceededP1(@Param("sla") String sla, @Param("year") Integer year,
            @Param("number_of_times") Integer number_of_times);

    static final String numberOfTimesNotsucceededP2 = " select date_part('Month', sla_date), count(*) " +
            "from sla_daily_consolidation " +
            "where sla_name = :sla " +
            "and date_part('Year', sla_date) = :year " +
            "and p2_success = 'false' " +
            "group by date_part('Month', sla_date) " +
            "having count(*) > :number_of_times";

    @Query(value = numberOfTimesNotsucceededP2, nativeQuery = true)
    ArrayList<String> getNumberOfTimesNotsucceededP2(@Param("sla") String sla, @Param("year") Integer year,
            @Param("number_of_times") Integer number_of_times);

    static final String numberOfTimesNotsucceededP3 = " select date_part('Month', sla_date), count(*) " +
            "from sla_daily_consolidation " +
            "where sla_name = :sla " +
            "and date_part('Year', sla_date) = :year " +
            "and p3_success = 'false' " +
            "group by date_part('Month', sla_date) " +
            "having count(*) > :number_of_times";

    @Query(value = numberOfTimesNotsucceededP3, nativeQuery = true)
    ArrayList<String> getNumberOfTimesNotsucceededP3(@Param("sla") String sla, @Param("year") Integer year,
            @Param("number_of_times") Integer number_of_times);

    static final String numberOfTimesNotsucceededOverall = " select date_part('Month', sla_date), count(*) " +
            "from sla_daily_consolidation " +
            "where sla_name = :sla " +
            "and date_part('Year', sla_date) = :year " +
            "and overall_success = 'false' " +
            "group by date_part('Month', sla_date) " +
            "having count(*) > :number_of_times";

    @Query(value = numberOfTimesNotsucceededOverall, nativeQuery = true)
    ArrayList<String> getNumberOfTimesNotsucceededOverall(@Param("sla") String sla, @Param("year") Integer year,
            @Param("number_of_times") Integer number_of_times);

    static final String numberOfTimesP1 = "SELECT COUNT(*) " +
            "FROM sla_daily_consolidation " +
            "WHERE sla_name = :sla_name " +
            "AND EXTRACT(MONTH FROM sla_date) = :mes " +
            "AND EXTRACT(YEAR FROM sla_date) = :anio " +
            "AND p1_success = 'false'";

    @Query(value = numberOfTimesP1, nativeQuery = true)
    Integer getNumberOfTimesP1(@Param("sla_name") String sla_name, @Param("anio") Integer anio,
            @Param("mes") Integer mes);

    static final String numberOfTimesP2 = "SELECT COUNT(*) " +
            "FROM sla_daily_consolidation " +
            "WHERE sla_name = :sla_name " +
            "AND EXTRACT(MONTH FROM sla_date) = :mes " +
            "AND EXTRACT(YEAR FROM sla_date) = :anio " +
            "AND p2_success = 'false'";

    @Query(value = numberOfTimesP2, nativeQuery = true)
    Integer getNumberOfTimesP2(@Param("sla_name") String sla_name, @Param("anio") Integer anio,
            @Param("mes") Integer mes);

    static final String numberOfTimesP3 = "SELECT COUNT(*) " +
            "FROM sla_daily_consolidation " +
            "WHERE sla_name = :sla_name " +
            "AND EXTRACT(MONTH FROM sla_date) = :mes " +
            "AND EXTRACT(YEAR FROM sla_date) = :anio " +
            "AND p3_success = 'false'";

    @Query(value = numberOfTimesP3, nativeQuery = true)
    Integer getNumberOfTimesP3(@Param("sla_name") String sla_name, @Param("anio") Integer anio,
            @Param("mes") Integer mes);

    static final String numberOfTimesOverall = "SELECT COUNT(*) " +
            "FROM sla_daily_consolidation " +
            "WHERE sla_name = :sla_name " +
            "AND EXTRACT(MONTH FROM sla_date) = :mes " +
            "AND EXTRACT(YEAR FROM sla_date) = :anio " +
            "AND overall_success = 'false'";

    @Query(value = numberOfTimesOverall, nativeQuery = true)
    Integer getNumberOfTimesOverall(@Param("sla_name") String sla_name, @Param("anio") Integer anio,
            @Param("mes") Integer mes);

}
