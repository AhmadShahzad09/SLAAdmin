package com.slatoolapi.administrationservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.slatoolapi.administrationservice.entity.SLAExecution;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsObjectiveRepository;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsRepository;
import com.slatoolapi.administrationservice.repository.SLADataMeterRepository;
import com.slatoolapi.administrationservice.repository.SLAExecutionRepository;
import com.slatoolapi.administrationservice.repository.SLAScheduleRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class SLAAsyncExecutionService {
        @Autowired
        private SLAScheduleService slaScheduleService;

        @Autowired
        private SLAExecutionRepository slaExecutionRepository;

        @Autowired
        private SLAScheduleRepository sLAScheduleRepository;

        @Autowired
        private CalculationResultService calculationResultService;

        @Autowired
        private CalculationService calculationService;

        @Autowired
        private CalculationDataEntry calculationDataEntry;

        @Autowired
        private SLACalculationMethodsRepository slaCalculationMethodsRepository;

        @Autowired
        private SLACalculationMethodsObjectiveRepository slaCalculationMethodsObjectiveRepository;

        @Autowired
        private SLADataMeterRepository slaDataMeterRepository;

        public void execution(long scheduleId) throws SQLException, ClassNotFoundException {
                try {

                        Long calculationId = slaScheduleService.getById(scheduleId).getCalculationMethodsId()
                                        .getCalculationModuleId().getId();

                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                        List<SLAExecution> slaExecutions = slaExecutionRepository.findByScheduleIdStatus(scheduleId);

                        for (SLAExecution slaExecution : slaExecutions) {
                                if (!slaExecution.getAudit()) {
                                        forExecution(slaExecution, calculationId);

                                } else {
                                        forAudit(slaExecution, calculationId);

                                }

                        }

                } catch (Exception ex) {
                        log.info("error - getLocalizedMessage -> {}", ex.getLocalizedMessage());
                        log.info("error - getMessage -> {}", ex.getMessage());
                }

        }

        //@Async("useAsyncA")
        public void forExecution(SLAExecution slaExecution, Long calculationId)
                        throws SQLException, ClassNotFoundException {
                try {

                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                        String dateExecution = slaExecution.getSlaDate()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        LocalDate dateTwoDaysBack = slaExecution.getSlaDate().plusDays(-2);
                        String dateTwo = dateTwoDaysBack
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        slaExecution.setStartDateTime(LocalDateTime
                                        .parse(LocalDateTime.now().format(outputFormatter)));

                        float overallAchieved = 0;
                        float p1Achieved = 0;
                        float p2Achieved = 0;
                        float p3Achieved = 0;

                        float overallMetersAffected = 0;
                        float p1MetersAffected = 0;
                        float p2MetersAffected = 0;
                        float p3MetersAffected = 0;

                        Long calculationMethodsId;
                        Integer limitTimesOverall;
                        Integer limitTimesP1;
                        Integer limitTimesP2;
                        Integer limitTimesP3;

                        Integer intCalculationId = calculationId.intValue();

                        int ofError;
                        Map<Boolean, Float> getOverallAchieved;

                        switch (intCalculationId) {
                                case 1:
                                        log.info("-SL#26-HES-----Configuration of meter for time of use------case-1-");

                                        overallAchieved = calculationService
                                                        .ConfigurationofMeterforTimeofUseOverall("P1",
                                                                        "P2",
                                                                        "P3", dateExecution);
                                        p1Achieved = calculationService
                                                        .ConfigurationofMeterforTimeofUse("P1",
                                                                        dateExecution);
                                        p2Achieved = calculationService
                                                        .ConfigurationofMeterforTimeofUse("P2",
                                                                        dateExecution);
                                        p3Achieved = calculationService
                                                        .ConfigurationofMeterforTimeofUse("P3",
                                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .ConfigurationofMeterforTimeofUseAffectsOverall(
                                                                        "P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService
                                                        .ConfigurationofMeterforTimeofUseAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .ConfigurationofMeterforTimeofUseAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .ConfigurationofMeterforTimeofUseAffects("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 2:
                                        log.info("KSL#03-TEST-HES-----Network Availability------case-2-");

                                        overallAchieved = 0;

                                        p1Achieved = calculationService.NetworkAvailability("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.NetworkAvailability("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.NetworkAvailability("P3",
                                                        dateExecution);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .NetworkAvailabilityAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .NetworkAvailabilityAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .NetworkAvailabilityAffects("P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 3:
                                        log.info("-SL#27-HES-----Configuration of meter for prepaid------case-3-");

                                        overallAchieved = calculationService.SLA79CalculatedOverall(
                                                        "P1", "P2", "P3",
                                                        dateExecution);
                                        p1Achieved = calculationService.SLA79Calculated("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.SLA79Calculated("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.SLA79Calculated("P3",
                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .SLA79CalculatedAffectsOv("P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService
                                                        .SLA79CalculatedAffects("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .SLA79CalculatedAffects("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .SLA79CalculatedAffects("P3", dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 4:
                                        log.info("-SL#31-HES-----Remote load control------case-4-");

                                        overallAchieved = calculationService.SLA81CalculatedOverall(
                                                        "P1", "P2", "P3",
                                                        dateExecution);
                                        p1Achieved = calculationService.SLA81Calculated("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.SLA81Calculated("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.SLA81Calculated("P3",
                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .SLA81CalculatedAffectsOv("P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService
                                                        .SLA81CalculatedAffects("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .SLA81CalculatedAffects("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .SLA81CalculatedAffects("P3", dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 5:
                                        log.info("-SL#30-HES-----Automatic service reduction/interruption------case-5-");

                                        overallAchieved = calculationService.AutomaticServiceRIOverall(
                                                        "P1", "P2", "P3",
                                                        dateExecution);
                                        p1Achieved = calculationService.AutomaticServiceRI("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.AutomaticServiceRI("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.AutomaticServiceRI("P3",
                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .AutomaticServiceRIOverallAffects("P1", "P2",
                                                                        "P3", dateExecution);
                                        p1MetersAffected = calculationService
                                                        .AutomaticServiceRIAffects("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .AutomaticServiceRIAffects("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .AutomaticServiceRIAffects("P3", dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 6:
                                        log.info("-KSL#07-Tolerance-----Remote connect/disconnect------case-6-");

                                        overallAchieved = calculationService.RemoteCDServiceOverall(
                                                        "P1", "P2", "P3",
                                                        dateExecution);
                                        p1Achieved = calculationService
                                                        .RemoteConnectDisconnectforSelectedConsumers(
                                                                        "P1",
                                                                        dateExecution);
                                        p2Achieved = calculationService
                                                        .RemoteConnectDisconnectforSelectedConsumers(
                                                                        "P2",
                                                                        dateExecution);
                                        p3Achieved = calculationService
                                                        .RemoteConnectDisconnectforSelectedConsumers(
                                                                        "P3",
                                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .RemoteCDOverallAffects("P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService.RemoteCDAffects("P1",
                                                        dateExecution);
                                        p2MetersAffected = calculationService.RemoteCDAffects("P2",
                                                        dateExecution);
                                        p3MetersAffected = calculationService.RemoteCDAffects("P3",
                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");
                                        break;

                                case 7:
                                        log.info("-SL#24-HES-----Remote data acquisition------case-7-");

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.remoteDataAcquisition("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.remoteDataAcquisition("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.remoteDataAcquisition("P3",
                                                        dateExecution);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffects("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 34:
                                        log.info("-KSL#08-Tolerance-----Remote data acquisition - Tolerance------case-34-");

                                        overallAchieved = 0;

                                        p1Achieved = calculationService.remoteDataAcquisitionTolerance(
                                                        "P1", dateExecution);
                                        p2Achieved = calculationService.remoteDataAcquisitionTolerance(
                                                        "P2", dateExecution);
                                        p3Achieved = calculationService.remoteDataAcquisitionTolerance(
                                                        "P3", dateExecution);

                                        overallMetersAffected = 0;

                                        p1MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffectsTolerance("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffectsTolerance("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .remoteDataAcquisitionAffectsTolerance("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 8:
                                        log.info("-SL#01-HES-----Not Connecting Meters------case-8-");

                                        overallAchieved = 0;

                                        Map<Boolean, Float> getP1 = calculationDataEntry
                                                        .NotConnectingMetersP1("1", dateExecution);
                                        Map<Boolean, Float> getP2 = calculationDataEntry
                                                        .NotConnectingMetersP2("2", dateExecution);
                                        Map<Boolean, Float> getP3 = calculationDataEntry
                                                        .NotConnectingMetersP3("3", dateExecution);

                                        ofError = 0;

                                        for (Map.Entry<Boolean, Float> pair : getP3.entrySet()) {
                                                if (pair.getKey()) {
                                                        p3Achieved = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        for (Map.Entry<Boolean, Float> pair : getP2.entrySet()) {
                                                if (pair.getKey()) {
                                                        p2Achieved = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        for (Map.Entry<Boolean, Float> pair : getP1.entrySet()) {
                                                if (pair.getKey()) {
                                                        p1Achieved = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        if (ofError == 0) {
                                                slaExecution.setStatus("done");
                                                slaExecution.setExecutionResult("ok");
                                        } else {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                        }

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationDataEntry
                                                        .NotConnectingMetersAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationDataEntry
                                                        .NotConnectingMetersAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationDataEntry
                                                        .NotConnectingMetersAffects("P3",
                                                                        dateExecution);

                                        break;

                                case 9:
                                        log.info("-SL#13-HES-----Alarm in device until message reaches customer------case-9-");

                                        overallAchieved = 0;
                                        p1Achieved = 0;
                                        p2Achieved = 0;
                                        p3Achieved = 0;

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesOverall = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId,
                                                                        "Overall")
                                                        .getLimitTimes();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesOverall = (limitTimesOverall == null) ? 0 : limitTimesOverall;
                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallMetersAffected = calculationService
                                                        .AlarmInDeviceUntilMessageReachesCustomerOverallAffected(
                                                                        "P1", "P2", "P3",
                                                                        dateExecution, limitTimesOverall);
                                        p1MetersAffected = calculationService
                                                        .AlarmInDeviceUntilMessageReachesCustomerP1Affected(
                                                                        "P1", dateExecution, limitTimesP1);
                                        p2MetersAffected = calculationService
                                                        .AlarmInDeviceUntilMessageReachesCustomerP2Affected(
                                                                        "P2", dateExecution, limitTimesP2);
                                        p3MetersAffected = calculationService
                                                        .AlarmInDeviceUntilMessageReachesCustomerP3Affected(
                                                                        "P3", dateExecution, limitTimesP3);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 10:
                                        log.info("-SL#28-HES-----Measurement of power indicators------case-10-");
                                        overallAchieved = calculationService
                                                        .MeasurementOfPowerIndicatorsOverall("P1", "P2",
                                                                        "P3",
                                                                        dateExecution);
                                        p1Achieved = calculationService.MeasurementOfPowerIndicators(
                                                        "P1", dateExecution);
                                        p2Achieved = calculationService.MeasurementOfPowerIndicators(
                                                        "P2", dateExecution);
                                        p3Achieved = calculationService.MeasurementOfPowerIndicators(
                                                        "P3", dateExecution);

                                        overallMetersAffected = calculationService
                                                        .MeasurementOfPowerIndicatorsAffectsOverall(
                                                                        "P1",
                                                                        "P2", "P3", dateExecution);
                                        p1MetersAffected = calculationService
                                                        .MeasurementOfPowerIndicatorsAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .MeasurementOfPowerIndicatorsAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .MeasurementOfPowerIndicatorsAffects("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 11:
                                        log.info("-SL#22-HES-----Remotely read event logs------case-11-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.RemotelyReadEventLogsPx("P1",
                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.RemotelyReadEventLogsPx("P2",
                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.RemotelyReadEventLogsPx("P3",
                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .RemotelyReadEventLogsAffectedPx("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemotelyReadEventLogsAffectedPx("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemotelyReadEventLogsAffectedPx("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 12:
                                        log.info("-SL#21-HES-----Remotely alter settings in Meter/firmware upgrade------case-12-");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.RemotelyAlterSettingsPx("P1",
                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.RemotelyAlterSettingsPx("P2",
                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.RemotelyAlterSettingsPx("P3",
                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .RemotelyAlterSettingsAffectedPx("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemotelyAlterSettingsAffectedPx("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemotelyAlterSettingsAffectedPx("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 35:
                                        log.info("-SL#09-HES-----Max. Transmission delay for switching commands (Bulk data)------case-35-");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.MaxTransDelaySwitchCommandsPx("P1",
                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.MaxTransDelaySwitchCommandsPx("P2",
                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.MaxTransDelaySwitchCommandsPx("P3",
                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .MaxTransDelaySwitchCommandsAffectedPx("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .MaxTransDelaySwitchCommandsAffectedPx("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .MaxTransDelaySwitchCommandsAffectedPx("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 36:
                                        log.info("-SL#10-HES-----Max. Transmission delay for updates (Bulk data)------case-36-");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.MaxTransDelayUpdatesPx("P1",
                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.MaxTransDelayUpdatesPx("P2",
                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.MaxTransDelayUpdatesPx("P3",
                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .MaxTransDelayUpdatesAffectedPx("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .MaxTransDelayUpdatesAffectedPx("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .MaxTransDelayUpdatesAffectedPx("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 13:
                                        log.info("-SL#20-HES-----Meter loss of supply and outage detection------case-13-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.MeterLossOfSupplyPx("P1",
                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.MeterLossOfSupplyPx("P2",
                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.MeterLossOfSupplyPx("P3",
                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .MeterLossOfSupplyAffectPx("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .MeterLossOfSupplyAffectPx("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .MeterLossOfSupplyAffectPx("P3", dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 14:
                                        log.info("-SL#19-HES-----Remote connect/disconnect for selected consumers------case-14-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();
                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersPx(
                                                                        "P1",
                                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersPx(
                                                                        "P2",
                                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersPx(
                                                                        "P3",
                                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersAffectedP1(
                                                                        "P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersAffectedP2(
                                                                        "P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemoteConnectDisconnectForSelectedConsumersAffectedP3(
                                                                        "P3", dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 15:
                                        log.info("-SL#18-HES-----Remote load control commands------case-15-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService.RemoteLoadControlCommandsPx(
                                                        "P1", dateExecution, limitTimesP1);
                                        p2Achieved = calculationService.RemoteLoadControlCommandsPx(
                                                        "P2", dateExecution, limitTimesP2);
                                        p3Achieved = calculationService.RemoteLoadControlCommandsPx(
                                                        "P3", dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .RemoteLoadControlCommandsAffectedPx("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemoteLoadControlCommandsAffectedPx("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemoteLoadControlCommandsAffectedPx("P3",
                                                                        dateExecution);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 16:
                                        log.info("-SL#14-HES-----Reconnections from customer care center to energy supply------case-16-");
                                        overallAchieved = 0;
                                        p1Achieved = 0;
                                        p2Achieved = 0;
                                        p3Achieved = 0;

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();
                                        limitTimesOverall = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId,
                                                                        "Overall")
                                                        .getLimitTimes();
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesOverall = (limitTimesOverall == null) ? 0 : limitTimesOverall;
                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallMetersAffected = calculationService
                                                        .ReconnectionsFromCustomerAffectedOverall("P1",
                                                                        "P2", "P3", dateExecution, limitTimesOverall);
                                        p1MetersAffected = calculationService
                                                        .ReconnectionsFromCustomerP1Affects("P1",
                                                                        dateExecution, limitTimesP1);
                                        p2MetersAffected = calculationService
                                                        .ReconnectionsFromCustomerP2Affects("P2",
                                                                        dateExecution, limitTimesP2);
                                        p3MetersAffected = calculationService
                                                        .ReconnectionsFromCustomerP3Affects("P3",
                                                                        dateExecution, limitTimesP3);
                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 17:
                                        log.info("-falta-in-db------Exceeding the maximum Planned Outage occasions or duration------case-17-");

                                        getOverallAchieved = calculationDataEntry
                                                        .ExceedingTheMaximumPlannedOutageOccasionsOrDuration(
                                                                        dateExecution);
                                        ofError = 0;

                                        for (Map.Entry<Boolean, Float> pair : getOverallAchieved
                                                        .entrySet()) {
                                                if (pair.getKey()) {
                                                        overallAchieved = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        if (ofError == 0) {
                                                slaExecution.setStatus("done");
                                                slaExecution.setExecutionResult("ok");
                                        } else {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                        }

                                        break;

                                case 18:
                                        log.info("-SL#04-HES-----Exceeding the maximum Unplanned Outage occasions or duration------case-18-");
                                        overallAchieved = calculationDataEntry
                                                        .ExceedingTheMaximumUnplannedOutageOccasionsOrDuration(
                                                                        dateExecution);
                                        if (overallAchieved == 0) {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                        }

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 19:
                                        log.info("-SL#02-HES-----New connection------case-19-");

                                        Map<Boolean, Float> getP1Affected = calculationDataEntry
                                                        .newConnectionAffectedP1("p1",
                                                                        dateExecution);
                                        Map<Boolean, Float> getP2Affected = calculationDataEntry
                                                        .newConnectionAffectedP2("p2",
                                                                        dateExecution);
                                        Map<Boolean, Float> getP3Affected = calculationDataEntry
                                                        .newConnectionAffectedP3("p3",
                                                                        dateExecution);

                                        ofError = 0;

                                        for (Map.Entry<Boolean, Float> pair : getP1Affected
                                                        .entrySet()) {
                                                if (pair.getKey()) {
                                                        p1MetersAffected = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        for (Map.Entry<Boolean, Float> pair : getP2Affected
                                                        .entrySet()) {
                                                if (pair.getKey()) {
                                                        p2MetersAffected = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        for (Map.Entry<Boolean, Float> pair : getP3Affected
                                                        .entrySet()) {
                                                if (pair.getKey()) {
                                                        p3MetersAffected = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        if (ofError == 0) {
                                                slaExecution.setStatus("done");
                                                slaExecution.setExecutionResult("ok");
                                        } else {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                        }

                                        break;

                                case 20:
                                        log.info("-SL#03-HES-----Exceeding the maximum Planned Outage - HES------case-20-");
                                        getOverallAchieved = calculationDataEntry
                                                        .ExceedingTheMaximumPlannedOutageHES(
                                                                        dateExecution);
                                        ofError = 0;

                                        for (Map.Entry<Boolean, Float> pair : getOverallAchieved
                                                        .entrySet()) {
                                                if (pair.getKey()) {
                                                        overallAchieved = pair.getValue();
                                                } else {
                                                        ofError = +1;
                                                }
                                        }

                                        if (ofError == 0) {
                                                slaExecution.setStatus("done");
                                                slaExecution.setExecutionResult("ok");
                                        } else {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                        }

                                        break;

                                case 21:
                                        log.info("-SL#05-HES-----Network availability - HES------case-21-");
                                        overallAchieved = 0;
                                        p1Achieved = calculationService.NetworkAvailabilityHES("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.NetworkAvailabilityHES("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.NetworkAvailabilityHES("P3",
                                                        dateExecution);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .NetworkAvailabilityAffectsHES("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .NetworkAvailabilityAffectsHES("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .NetworkAvailabilityAffectsHES("P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 22:
                                        log.info("-SL#23-HES-----Read the event logs pertaining to all Meters------case-22-");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();

                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();

                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();
                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        log.info("overallAchieved -> {}", overallAchieved);
                                        p1Achieved = calculationService
                                                        .ReadTheEventLogsPertainingToAllMeters("P1",
                                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService
                                                        .ReadTheEventLogsPertainingToAllMeters("P2",
                                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService
                                                        .ReadTheEventLogsPertainingToAllMeters("P3",
                                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .ReadTheEventLogsPertainingToAllMetersAffected(
                                                                        "P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .ReadTheEventLogsPertainingToAllMetersAffected(
                                                                        "P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .ReadTheEventLogsPertainingToAllMetersAffected(
                                                                        "P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 23:
                                        log.info("-falta------Integration of DER------case-23-");
                                        overallAchieved = calculationService.IntegrationOfDERPxOverall(
                                                        "P1", "P2", "P3",
                                                        dateExecution);
                                        p1Achieved = calculationService.IntegrationOfDERPx("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.IntegrationOfDERPx("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.IntegrationOfDERPx("P3",
                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .IntegrationOfDERPxAffectsOverall("P1", "P2",
                                                                        "P3", dateExecution);
                                        p1MetersAffected = calculationService
                                                        .IntegrationOfDERPxAffects("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .IntegrationOfDERPxAffects("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .IntegrationOfDERPxAffects("P3", dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 24:
                                        log.info("-falta------Remote appliance control on demand------case-24-");
                                        overallAchieved = calculationService
                                                        .RemoteApplianceControlOnDemandOverall("P1",
                                                                        "P2", "P3",
                                                                        dateExecution);
                                        p1Achieved = calculationService
                                                        .RemoteApplianceControlOnDemandPx("P1",
                                                                        dateExecution);
                                        p2Achieved = calculationService
                                                        .RemoteApplianceControlOnDemandPx("P2",
                                                                        dateExecution);
                                        p3Achieved = calculationService
                                                        .RemoteApplianceControlOnDemandPx("P3",
                                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .RemoteApplianceControlOnDemandAffectsOverall(
                                                                        "P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService
                                                        .RemoteApplianceControlOnDemandAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemoteApplianceControlOnDemandAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemoteApplianceControlOnDemandAffects("P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 25:
                                        log.info("-falta------Remote control of decentralized generation up to shut down------case-25-");
                                        overallAchieved = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownOverall(
                                                                        "P1", "P2", "P3",
                                                                        dateExecution);
                                        p1Achieved = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownPx(
                                                                        "P1",
                                                                        dateExecution);
                                        p2Achieved = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownPx(
                                                                        "P2",
                                                                        dateExecution);
                                        p3Achieved = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownPx(
                                                                        "P3",
                                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownAffectsOverall(
                                                                        "P1", "P2", "P3",
                                                                        dateExecution);
                                        p1MetersAffected = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownAffects(
                                                                        "P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownAffects(
                                                                        "P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .RemoteControlOfDecentralizedGenerationUpToShutDownAffects(
                                                                        "P3", dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 26:
                                        log.info("-SL#29-HES-----Meter alarms/logged events------case-26-");
                                        overallAchieved = calculationService
                                                        .MeterAlarmsLoggedEventsOverall("P1", "P2",
                                                                        "P3",
                                                                        dateExecution);
                                        p1Achieved = calculationService.MeterAlarmsLoggedEvents("P1",
                                                        dateExecution);
                                        p2Achieved = calculationService.MeterAlarmsLoggedEvents("P2",
                                                        dateExecution);
                                        p3Achieved = calculationService.MeterAlarmsLoggedEvents("P3",
                                                        dateExecution);

                                        overallMetersAffected = calculationService
                                                        .MeterAlarmsLoggedEventsAffectsOverall("P1",
                                                                        "P2",
                                                                        "P3", dateExecution);
                                        p1MetersAffected = calculationService
                                                        .MeterAlarmsLoggedEventsAffects("P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .MeterAlarmsLoggedEventsAffects("P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .MeterAlarmsLoggedEventsAffects("P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 27:
                                        log.info("-SL#16-HES-----Collection of daily Meter interval reading - HES------case-27-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        overallAchieved = 0;
                                        p1Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHES("P1",
                                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHES("P2",
                                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHES("P3",
                                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 28:
                                        log.info("-SL#17-HES-----Individual read - HES------case-28-");
                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        log.info("calculationMethodsId -- ok");
                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();

                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();

                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        log.info("A- limitTimeP123 -- ok", "null o valor");

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        log.info("B- limitTimeP123 -- ok", limitTimesP1, " ", limitTimesP2, " ",
                                                        limitTimesP3);

                                        p1Achieved = calculationService.IndividualReadHESPx("P1",
                                                        dateExecution, limitTimesP1);
                                        log.info("p1Achieved -- ok");

                                        p2Achieved = calculationService.IndividualReadHESPx("P2",
                                                        dateExecution, limitTimesP2);

                                        log.info("p2Achieved -- ok");

                                        p3Achieved = calculationService.IndividualReadHESPx("P3",
                                                        dateExecution, limitTimesP3);

                                        log.info("p3Achieved -- ok");

                                        p1MetersAffected = calculationService
                                                        .IndividualReadHESAffects("P1", dateExecution);
                                        p2MetersAffected = calculationService
                                                        .IndividualReadHESAffects("P2", dateExecution);
                                        p3MetersAffected = calculationService
                                                        .IndividualReadHESAffects("P3", dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 29:
                                        log.info("-SL#06-HES-----Spatial availability - HES------case-29-");

                                        if (!slaDataMeterRepository
                                                        .getSpatialAvailabilityHesZone(dateExecution)
                                                        .isEmpty()) {

                                                log.info("zone {}", slaExecution.getZone());
                                                String zone;

                                                if (slaExecution.getZone() == null) {
                                                        zone = "";
                                                } else {
                                                        zone = slaExecution.getZone();
                                                }

                                                overallAchieved = calculationService
                                                                .SpatialAvailabilityHESOverall(zone,
                                                                                "P1", "P2",
                                                                                "P3", dateExecution);
                                                p1Achieved = calculationService.SpatialAvailabilityHES(
                                                                zone, "P1", dateExecution);
                                                p2Achieved = calculationService.SpatialAvailabilityHES(
                                                                zone, "P2", dateExecution);
                                                p3Achieved = calculationService.SpatialAvailabilityHES(
                                                                zone, "P3", dateExecution);

                                                overallMetersAffected = calculationService
                                                                .SpatialAvailabilityHESOverallAffects(
                                                                                zone,
                                                                                "P1", "P2", "P3",
                                                                                dateExecution);
                                                p1MetersAffected = calculationService
                                                                .SpatialAvailabilityHESAffects(zone,
                                                                                "P1",
                                                                                dateExecution);
                                                p2MetersAffected = calculationService
                                                                .SpatialAvailabilityHESAffects(zone,
                                                                                "P2",
                                                                                dateExecution);
                                                p3MetersAffected = calculationService
                                                                .SpatialAvailabilityHESAffects(zone,
                                                                                "P3",
                                                                                dateExecution);

                                                log.info("-------DONE-------");
                                                slaExecution.setStatus("done");
                                                slaExecution.setExecutionResult("ok");
                                                log.info("-------overallAchieved=" + overallAchieved);
                                                slaExecution.setOverallAchieved(overallAchieved);
                                                log.info("-------p1Achieved=" + p1Achieved);
                                                slaExecution.setP1Achieved(p1Achieved);
                                                log.info("-------p2Achieved=" + p2Achieved);
                                                slaExecution.setP2Achieved(p2Achieved);
                                                log.info("-------p3Achieved=" + p3Achieved);
                                                slaExecution.setP3Achieved(p3Achieved);
                                                log.info("-------overallMetersAffected="
                                                                + overallMetersAffected);
                                                slaExecution.setOverallMetersAffected(
                                                                overallMetersAffected);
                                                log.info("-------p1MetersAffected=" + p1MetersAffected);
                                                slaExecution.setP1MetersAffected(p1MetersAffected);
                                                log.info("-------p2MetersAffected=" + p2MetersAffected);
                                                slaExecution.setP2MetersAffected(p2MetersAffected);
                                                log.info("-------p3MetersAffected=" + p3MetersAffected);
                                                slaExecution.setP3MetersAffected(p3MetersAffected);
                                                slaExecution.setEndDateTime(LocalDateTime
                                                                .parse(LocalDateTime.now().format(
                                                                                outputFormatter)));

                                                slaExecutionRepository.save(slaExecution);

                                        } else {
                                                slaExecution.setStatus("error");
                                                slaExecution.setExecutionResult("Not ok");
                                                slaExecutionRepository.save(slaExecution);
                                        }

                                        break;

                                case 30:
                                        log.info("-KSL#06------Collection of daily Meter interval reading - Tolerance 1------case-30-");
                                        overallAchieved = 0;

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        p1Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P1",
                                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P2",
                                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P3",
                                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 31:
                                        log.info("-KSL#06------Collection of daily Meter interval reading - Tolerance 2------case-31-");
                                        overallAchieved = 0;

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaExecution.getSlaName()).getId();

                                        limitTimesP1 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p1")
                                                        .getLimitTimes();
                                        limitTimesP2 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p2")
                                                        .getLimitTimes();
                                        limitTimesP3 = slaCalculationMethodsObjectiveRepository
                                                        .getByCalculationMethodsIdAndType(calculationMethodsId, "p3")
                                                        .getLimitTimes();

                                        limitTimesP1 = (limitTimesP1 == null) ? 0 : limitTimesP1;
                                        limitTimesP2 = (limitTimesP2 == null) ? 0 : limitTimesP2;
                                        limitTimesP3 = (limitTimesP3 == null) ? 0 : limitTimesP3;

                                        p1Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P1",
                                                                        dateExecution, limitTimesP1);
                                        p2Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P2",
                                                                        dateExecution, limitTimesP2);
                                        p3Achieved = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESPx(
                                                                        "P3",
                                                                        dateExecution, limitTimesP3);

                                        overallMetersAffected = 0;
                                        p1MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P1",
                                                                        dateExecution);
                                        p2MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P2",
                                                                        dateExecution);
                                        p3MetersAffected = calculationService
                                                        .CollectionOfDailyMeterIntervalReadingHESAffects(
                                                                        "P3",
                                                                        dateExecution);

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                case 33:
                                        log.info(
                                                        "-KSL#05-Tolerance-----Reconnections from customer care center to energy supply - Tolerance------case-33-");

                                        overallAchieved = 0;
                                        p1Achieved = calculationService
                                                        .ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(
                                                                        "P1", dateExecution, "OnDemandDisconnect",
                                                                        "FINISH_OK");
                                        p2Achieved = calculationService
                                                        .ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(
                                                                        "P2", dateExecution, "OnDemandDisconnect",
                                                                        "FINISH_OK");
                                        p3Achieved = calculationService
                                                        .ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(
                                                                        "P3", dateExecution, "OnDemandDisconnect",
                                                                        "FINISH_OK");

                                        overallMetersAffected = 0;
                                        p1MetersAffected = 0;
                                        p2MetersAffected = 0;
                                        p3MetersAffected = 0;

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");

                                        break;

                                default:
                                        break;
                        }

                        if (calculationId != 29) {
                                if (Objects.equals(slaExecution.getStatus(), "pending")) {
                                        slaExecution.setStatus("done");
                                }
                                if (!Objects.equals(slaExecution.getExecutionResult(), "ok")) {
                                        slaExecution.setExecutionResult("Not ok");
                                } else {
                                        slaExecution.setExecutionResult("ok");
                                }

                                slaExecution.setOverallAchieved(overallAchieved);

                                slaExecution.setP1Achieved(p1Achieved);

                                slaExecution.setP2Achieved(p2Achieved);

                                slaExecution.setP3Achieved(p3Achieved);

                                slaExecution.setOverallMetersAffected(overallMetersAffected);

                                slaExecution.setP1MetersAffected(p1MetersAffected);

                                slaExecution.setP2MetersAffected(p2MetersAffected);

                                slaExecution.setP3MetersAffected(p3MetersAffected);
                                slaExecution.setEndDateTime(LocalDateTime
                                                .parse(LocalDateTime.now().format(outputFormatter)));

                                slaExecutionRepository.save(slaExecution);
                        }

                } catch (Exception ex) {
                }

        }

        //@Async("useAsyncA")
        public void forAudit(SLAExecution slaExecution, Long calculationId)
                        throws SQLException, ClassNotFoundException {

                try {
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        slaExecution.setStartDateTime(LocalDateTime.parse(LocalDateTime.now().format(outputFormatter)));
                        String dateExecution = slaExecution.getSlaDate()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        LocalDate dateTwoDaysBack = slaExecution.getSlaDate().plusDays(-2);
                        String dateTwo = dateTwoDaysBack
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        Integer intCalculationId = calculationId.intValue();
                        List<Object[]> data = sLAScheduleRepository.queryDataEntryAudit();
                        Stream<Object[]> dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                        switch (intCalculationId) {
                                case 1:
                                        log.info("Audit--SL#26-HES-----Configuration of meter for time of use------case-1-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandSetTariffAgreement");
                                        break;

                                case 2:
                                        log.info("Audit-KSL#03-TEST-HES-----Network Availability------case-2-");

                                        dataStream = sLAScheduleRepository.queryNoCommandAuditStream(dateExecution);

                                        break;

                                case 3:
                                        log.info("Audit-SL#27-HES-----Configuration of meter for prepaid------case-3-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandSetPaymentMode", "OnDemandGetPaymentMode");

                                        break;

                                case 4:
                                        log.info("Audit--SL#31-HES-----Remote load control------case-4-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandSetLoadLimitation");

                                        break;

                                case 5:
                                        log.info("Audit--SL#30-HES-----Automatic service reduction/interruption------case-5-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandDisconnect", "OnDemandSetLoadLimitation");

                                        break;

                                case 6:
                                        log.info("Audit--KSL#07-Tolerance-----Remote connect/disconnect------case-6-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandDisconnect");

                                        break;

                                case 7:
                                        log.info("Audit--SL#24-HES-----Remote data acquisition------case-7-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit18Stream(dateExecution,
                                                        "OnDemandAbsoluteProfile",
                                                        "OnDemandWaterProfile",
                                                        "OnDemandReadMaximumDemandRegisters",
                                                        "OnDemandReadLoadProfile1",
                                                        "OnDemandReadInstantaneousValues",
                                                        "OnDemandReadBillingProfile",
                                                        "OnDemandGetLoadLimitThreshold",
                                                        "OnDemandGetBillingDate",
                                                        "OnDemandGetDemandIntegrationPeriod",
                                                        "OnDemandGetPaymentMode",
                                                        "OnDemandGetMeteringMode",
                                                        "OnDemandGetVoltRangeLow",
                                                        "OnDemandGetVoltRangeUp",
                                                        "OnDemandSetCurrentRangeLow",
                                                        "OnDemandSetCurrentRangeUp",
                                                        "OnDemandGetMeterStatus",
                                                        "OnDemandReadNamePlateInfoMeter",
                                                        "OnDemandGetTariffAgreement");

                                        break;

                                case 8:
                                        log.info("Audit--SL#01-HES-----Not Connecting Meters------case-8-");
                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                                        break;

                                case 9:
                                        log.info("Audit--SL#13-HES-----Alarm in device until message reaches customer------case-9-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit4Stream(dateExecution,
                                                        "OnAlarmNotification",
                                                        "OnConnectivityNotification",
                                                        "OnPowerDownNotification",
                                                        "OnInstallationNotification");

                                        break;

                                case 10:
                                        log.info("Audit--SL#28-HES-----Measurement of power indicators------case-10-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandReadLoadProfile1",
                                                        "OnDemandReadInstantaneousValues");

                                        break;

                                case 11:
                                        log.info("Audit--SL#22-HES-----Remotely read event logs------case-11-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandEventLog");

                                        break;

                                case 12:
                                        log.info("Audit--SL#21-HES-----Remotely alter settings in Meter/firmware upgrade------case-12-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit13Stream(dateExecution,

                                                        "OnDemandSetLoadProfileCapturePeriod",
                                                        "OnDemandSetLoadLimitation",
                                                        "OnDemandSetBillingDate",
                                                        "OnDemandClearAlarms",
                                                        "OnDemandSetDemandIntegrationPeriod",
                                                        "OnDemandSetPaymentMode",
                                                        "OnDemandSetMeteringMode",
                                                        "OnDemandSetVoltRangeLow",
                                                        "OnDemandSetVoltRangeUp",
                                                        "OnDemandGetCurrentRangeLow",
                                                        "OnDemandGetCurrentRangeUp",
                                                        "OnDemandSetTariffAgreement",
                                                        "OnDemandChangeMeterPassword");

                                        break;

                                case 13:
                                        log.info("Audit--SL#20-HES-----Meter loss of supply and outage detection------case-13-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit4Stream(dateExecution,
                                                        "OnAlarmNotification",
                                                        "OnConnectivityNotification",
                                                        "OnPowerDownNotification",
                                                        "OnInstallationNotification");

                                        break;

                                case 14:
                                        log.info("Audit--SL#19-HES-----Remote connect/disconnect for selected consumers------case-14-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandDisconnect",
                                                        "OnDemandConnect");

                                        break;

                                case 15:
                                        log.info("Audit--SL#18-HES-----Remote load control commands------case-15-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandSetLoadLimitation",
                                                        "OnDemandGetLoadLimitThreshold");

                                        break;

                                case 16:
                                        log.info("Audit--SL#14-HES-----Reconnections from customer care center to energy supply------case-16-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandConnect");

                                        break;

                                case 17:
                                        log.info("Audit--falta-in-db------Exceeding the maximum Planned Outage occasions or duration------case-17-");

                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                                        break;

                                case 18:
                                        log.info("Audit--SL#04-HES-----Exceeding the maximum Unplanned Outage occasions or duration------case-18-");

                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                                        break;

                                case 19:
                                        log.info("Audit--SL#02-HES-----New connection------case-19-");

                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                                        break;

                                case 20:
                                        log.info("Audit--SL#03-HES-----Exceeding the maximum Planned Outage - HES------case-20-");

                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();
                                        break;

                                case 21:
                                        log.info("Audit--SL#05-HES-----Network availability - HES------case-21-");
                                        dataStream = sLAScheduleRepository.queryNoCommandAuditStream(dateExecution);

                                        break;

                                case 22:
                                        log.info("Audit--SL#23-HES-----Read the event logs pertaining to all Meters------case-22-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandEventLog");

                                        break;

                                case 23:
                                        log.info("Audit--falta------Integration of DER------case-23-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution, "SLA32");
                                        break;

                                case 24:
                                        log.info("Audit--falta------Remote appliance control on demand------case-24-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution, "SLA33");
                                        break;

                                case 25:
                                        log.info("Audit--falta------Remote control of decentralized generation up to shut down------case-25-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution, "SLA34");
                                        break;

                                case 26:
                                        log.info("Audit--SL#29-HES-----Meter alarms/logged events------case-26-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandEventLog");

                                        break;

                                case 27:
                                	log.info("Audit--SL#16-HES-----Collection of daily Meter interval reading - HES------case-27-");

                                	// Definir el nombre del archivo CSV y su ruta
                                	String CSV_FOLDER_PATH = "/audit/" + slaExecution.getSlaName() + "_" + slaExecution.getId() + "/";
                                	
                                	createCsvFolder(slaExecution, CSV_FOLDER_PATH);

                                    // Definir el nombre del archivo CSV
                                    String CSV_FILE_NAME_PREFIX = "auditfile_" + slaExecution.getSlaName() + "_" + slaExecution.getId() + "_" ;
                                    
                                    String ZIP_FILE_PATH = "/audit/" + slaExecution.getSlaName() + "_" + slaExecution.getId() + ".zip";

                                    try {
                                        // Establecer el tamaño del lote
                                        int batchSize = 1000000; // Puedes ajustar este valor según tus necesidades

                                        // Obtener el total de datos
                                        int totalRows = sLAScheduleRepository.queryCountCommandAudit5(dateExecution, "WaterProfile", "EnergyProfile", "LoadProfile1");

                                        // Iterar en lotes
                                        int contadorFicheros = 0;
                                        for (int i = 0; i < totalRows; i += batchSize) {
                                        	
                                            // Crear el nombre del archivo CSV para este lote
                                            String CSV_FILE_NAME = CSV_FILE_NAME_PREFIX + contadorFicheros + ".csv";

                                            // Abrir el archivo CSV para escritura
                                            try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(CSV_FOLDER_PATH + CSV_FILE_NAME))) {
                                                // Escribir encabezados CSV
                                                String headers = "processDate,Priority,organization,serialNumber,status,zone,meterType,orderName,orderStatus,datetime,inittime,finishtime\n";
                                                csvWriter.write(headers);

                                                // Obtener el siguiente lote de datos
                                                List<Object[]> batch = sLAScheduleRepository.queryCommandAudit5Stream(dateExecution, "WaterProfile", "EnergyProfile", "LoadProfile1", batchSize, i);

                                                // Procesar y escribir cada fila en el archivo CSV
                                                batch.forEach(row -> {
                                                    try {
                                                        String csvRow = convertRowToCSV(row);
                                                        csvWriter.write(csvRow);
                                                        csvWriter.newLine(); // Agregar nueva línea después de cada fila
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                                contadorFicheros++;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    calculationResultService.zipDirectory(CSV_FOLDER_PATH, ZIP_FILE_PATH);
                                    deleteDirectory(new File(CSV_FOLDER_PATH));
                                    
                                    String urlFile = ZIP_FILE_PATH;

    	                         	slaExecution.setStatus("done");
    	                         	slaExecution.setExecutionResult("ok");
    	                         	slaExecution.setUrl(urlFile);
    	                         	slaExecution.setEndDateTime(LocalDateTime
    	                                         .parse(LocalDateTime.now().format(outputFormatter)));
    	                         	slaExecutionRepository.save(slaExecution);
                                    break;

                                case 28:
                                        log.info("Audit--SL#17-HES-----Individual read - HES------case-28-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "LoadProfile1");
                                    
                                        break;

                                case 29:
                                        log.info("Audit--SL#06-HES-----Spatial availability - HES------case-29-");

                                        dataStream = sLAScheduleRepository.queryNoCommandAuditStream(dateExecution);
                                        break;

                                case 30:
                                        log.info("Audit--KSL#06------Collection of daily Meter interval reading - Tolerance 1------case-30-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "WaterProfile", "EnergyProfile");

                                        break;

                                case 31:
                                        log.info("Audit--KSL#06------Collection of daily Meter interval reading - Tolerance 2------case-31-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "WaterProfile", "EnergyProfile");

                                        break;

                                case 32:
                                        log.info("Audit--KSL#06------Collection of daily Meter interval reading - Tolerance 2------case-31-");

                                        dataStream = sLAScheduleRepository.queryDataEntryAuditStream();

                                        break;

                                case 33:
                                        log.info(
                                                        "Audit--KSL#05-Tolerance-----Reconnections from customer care center to energy supply - Tolerance------case-33-");
                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandDisconnect");

                                        break;

                                case 34:
                                        log.info("Audit--KSL#08-Tolerance-----Remote data acquisition - Tolerance------case-34-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit2Stream(dateExecution,
                                                        "OnDemandAbsoluteProfile", "OnDemandWaterProfile");

                                        break;

                                case 35:
                                        log.info("Audit--SL#09-HES-----Max. Transmission delay for switching commands (Bulk data)------case-35-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit41Stream(dateExecution,

                                                        "OnDemandAbsoluteProfile",
                                                        "OnDemandWaterProfile",
                                                        "OnDemandReadMaximumDemandRegisters",
                                                        "LoadProfile2",
                                                        "OnDemandReadLoadProfile1",
                                                        "OnDemandReadLoadProfile2",
                                                        "OnDemandReadPowerQualityProfile",
                                                        "OnDemandReadInstrumentationProfile",
                                                        "OnDemandReadInstantaneousValues",
                                                        "OnDemandReadBillingProfile",
                                                        "OnDemandReadRealTime",
                                                        "OnDemandReadLoadProfileCapturePeriod",
                                                        "OnDemandSetLoadProfileCapturePeriod",
                                                        "OnDemandConnect",
                                                        "OnDemandDisconnect",
                                                        "OnDemandSwitchStatus",
                                                        "OnDemandSetMaximumDemand",
                                                        "OnDemandSetLoadLimitation",
                                                        "OnDemandGetLoadLimitThreshold",
                                                        "OnDemandSetBillingDate",
                                                        "OnDemandGetBillingDate",
                                                        "OnDemandClearAlarms",
                                                        "OnDemandSetDemandIntegrationPeriod",
                                                        "OnDemandGetDemandIntegrationPeriod",
                                                        "OnDemandSetPaymentMode",
                                                        "OnDemandGetPaymentMode",
                                                        "OnDemandSetMeteringMode",
                                                        "OnDemandGetMeteringMode",
                                                        "OnDemandSetVoltRangeLow",
                                                        "OnDemandSetVoltRangeUp",
                                                        "OnDemandGetVoltRangeLow",
                                                        "OnDemandGetVoltRangeUp",
                                                        "OnDemandGetCurrentRangeLow",
                                                        "OnDemandGetCurrentRangeUp",
                                                        "OnDemandSetCurrentRangeLow",
                                                        "OnDemandSetCurrentRangeUp",
                                                        "OnDemandGetMeterStatus",
                                                        "OnDemandGatewayReadNamePlateInfo",
                                                        "OnDemandReadNamePlateInfoMeter",
                                                        "OnDemandSetTariffAgreement",
                                                        "OnDemandGetTariffAgreement");

                                        break;

                                case 36:
                                        log.info("Audit--SL#10-HES-----Max. Transmission delay for updates (Bulk data)------case-36-");

                                        dataStream = sLAScheduleRepository.queryCommandAudit1Stream(dateExecution,
                                                        "OnDemandFirmwareUpgrade");

                                        break;

                                default:

                                        break;
                        }

                        try {
                        	if(intCalculationId != 27) {
                    
                        		String urlFile = calculationResultService.getAudit(dataStream, slaExecution.getId(),
                                        slaExecution.getSlaName());

	                         	slaExecution.setStatus("done");
	                         	slaExecution.setExecutionResult("ok");
	                         	slaExecution.setUrl(urlFile);
	                         	slaExecution.setEndDateTime(LocalDateTime
	                                         .parse(LocalDateTime.now().format(outputFormatter)));
	                         	slaExecutionRepository.save(slaExecution);
                        	}

                        	
                        
                        	log.info("Finalizado proceso Audit");

                        } catch (Exception e) {
                        }

                } catch (Exception ex) {
                }

        }
        private String convertRowToCSV(Object[] row) {
            StringBuilder csvRow = new StringBuilder();
            for (Object column : row) {
                csvRow.append(column != null ? column.toString() : "").append(",");
            }
            return csvRow.toString();
        }
        
        private void createCsvFolder(SLAExecution slaExecution, String folderPath) {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                    log.error("Error: No se pudo crear la carpeta para los archivos CSV.");
                    return;
                }
            }
        }
        
        private void deleteDirectory(File directory) {
            if (!directory.exists()) {
                return;
            }

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }

            directory.delete();
        }
        
        
}
