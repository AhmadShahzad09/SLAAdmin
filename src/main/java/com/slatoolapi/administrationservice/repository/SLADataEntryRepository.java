package com.slatoolapi.administrationservice.repository;

import com.slatoolapi.administrationservice.entity.SLADataEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface SLADataEntryRepository extends JpaRepository<SLADataEntry, Long> {

        static final String businessDataEntry = "SELECT business_start_date, business_end_date, data_type_id," +
                        "value FROM sla_data_entry as sde " +
                        "WHERE CAST(:date AS Date) BETWEEN sde.business_start_date " +
                        "and sde.business_end_date " +
                        "and sde.data_type_id = CAST(:typeId as Integer);";

        @Query(value = businessDataEntry, nativeQuery = true)
        List<String> businessDataEntry(@Param("date") String date, @Param("typeId") String typeId);

        static final String businessDataEntryVerified = "SELECT count(*) FROM sla_data_entry as sde " +
                        "WHERE CAST(:date AS Date) BETWEEN sde.business_start_date " +
                        "and sde.business_end_date " +
                        "and sde.data_type_id in (1,2,3)";

        @Query(value = businessDataEntryVerified, nativeQuery = true)
        Integer getBusinessDataEntryVerified(@Param("date") String date);

        static final String ExceedingTheMaximumPlannedOutageOccasionsOrDuration = "SELECT sde.business_start_date, " +
                        "sde.value " +
                        "FROM sla_data_entry as sde " +
                        "WHERE sde.data_type_id = '4'";

        @Query(value = ExceedingTheMaximumPlannedOutageOccasionsOrDuration, nativeQuery = true)
        ArrayList<String> getExceedingTheMaximumPlannedOutageOccasionsOrDuration();

        static final String timeLimitPlannedOutage = "SELECT ob.limit_times " +
                        "FROM sla_calculation_methods_objective as ob, " +
                        "sla_calculation_methods as sc " +
                        "WHERE sc.id = ob.calculation_methods_id " +
                        "and sc.calculation_module_id = '17' " +
                        "and ob.type = 'Overall' " +
                        "limit '1';";

        @Query(value = timeLimitPlannedOutage, nativeQuery = true)
        Float getTimeLimitPlannedOutage();

        static final String timeLimitPlannedOutageHES = "SELECT ob.limit_times " +
                        "FROM sla_calculation_methods_objective as ob, " +
                        "sla_calculation_methods as sc " +
                        "WHERE sc.id = ob.calculation_methods_id " +
                        "and sc.calculation_module_id = '20' " +
                        "and ob.type = 'Overall' " +
                        "limit '1';";

        @Query(value = timeLimitPlannedOutageHES, nativeQuery = true)
        Float getTimeLimitPlannedOutageHES();

        static final String ExceedingTheMaximumUnplannedOutageOccasionsOrDuration = "select sde.business_start_date, " +
                        "sde.value " +
                        "from sla_data_entry sde " +
                        "where sde.data_type_id in ('5', '9', '10', '11')";

        @Query(value = ExceedingTheMaximumUnplannedOutageOccasionsOrDuration, nativeQuery = true)
        ArrayList<String> getExceedingTheMaximumUnplannedOutageOccasionsOrDuration();

        static final String timeLimitUnplannedOutage = "SELECT ob.limit_times " +
                        "FROM sla_calculation_methods_objective as ob, " +
                        "sla_calculation_methods as sc " +
                        "WHERE sc.id = ob.calculation_methods_id " +
                        "and sc.calculation_module_id = '18' " +
                        "and ob.type = 'Overall' " +
                        "limit '1';";

        @Query(value = timeLimitUnplannedOutage, nativeQuery = true)
        Float getTimeLimitUnplannedOutage();

        static final String newConnectionTimeLimit = "SELECT count(*) " +
                        "FROM sla_calculation_methods_objective as ob, " +
                        "sla_calculation_methods as sc " +
                        "WHERE sc.id = ob.calculation_methods_id " +
                        "and sc.calculation_module_id = '19' " +
                        "and ob.type in (:px) " +
                        "and (date_part('day' ,((CAST(:date AS Date))))) > ob.limit_times;";

        @Query(value = newConnectionTimeLimit, nativeQuery = true)
        Float getNewConnectionTimeLimit(@Param("px") String px, @Param("date") String date);

        static final String totalAssignedMetersNewConnectionP1 = "select sde.business_start_date, " +
                        "sde.value " +
                        "from sla_data_entry sde " +
                        "where (CAST(sde.business_start_date AS Date) <= CAST(:date as Date)) " +
                        "and sde.data_type_id = '6'";

        @Query(value = totalAssignedMetersNewConnectionP1, nativeQuery = true)
        ArrayList<String> getTotalAssignedMetersNewConnectionP1(@Param("date") String date);

        static final String totalAssignedMetersNewConnectionP2 = "select sde.business_start_date, " +
                        "sde.value " +
                        "from sla_data_entry sde " +
                        "where (CAST(sde.business_start_date AS Date) <= CAST(:date as Date)) " +
                        "and sde.data_type_id = '7'";

        @Query(value = totalAssignedMetersNewConnectionP2, nativeQuery = true)
        ArrayList<String> getTotalAssignedMetersNewConnectionP2(@Param("date") String date);

        static final String totalAssignedMetersNewConnectionP3 = "select sde.business_start_date, " +
                        "sde.value " +
                        "from sla_data_entry sde " +
                        "where (CAST(sde.business_start_date AS Date) <= CAST(:date as Date)) " +
                        "and sde.data_type_id = '8'";

        @Query(value = totalAssignedMetersNewConnectionP3, nativeQuery = true)
        ArrayList<String> getTotalAssignedMetersNewConnectionP3(@Param("date") String date);

}
