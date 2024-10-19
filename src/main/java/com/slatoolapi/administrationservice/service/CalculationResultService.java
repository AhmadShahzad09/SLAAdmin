package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.*;
import com.slatoolapi.administrationservice.entity.*;
import com.slatoolapi.administrationservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class CalculationResultService {

        @Value("${spring.datasource.url}")
        private String databaseAddress;

        @Value("${spring.datasource.username}")
        private String username;

        @Value("${spring.datasource.password}")
        private String password;

        @Value("${spring.datasource.driver-class-name}")
        private String driverClassName;

        @Autowired
        private SLAScheduleRepository slaScheduleRepository;

        @Autowired
        private SLAExecutionService slaExecutionService;

        @Autowired
        private SLACalculationMethodsRepository slaCalculationMethodsRepository;

        @Autowired
        private SLAExecutionRepository slaExecutionRepository;

        @Autowired
        private CalculationResultRepository calculationResultRepository;

        @Autowired
        private SLADataMeterRepository slaDataMeterRepository;

        @Autowired
        private SLACalculationMethodsObjectiveRepository slaCalculationMethodsObjectiveRepository;

        public List<CalculationResultDto> getAll(boolean lastExecution) {
                log.info("LAST_EXECUTION {}", lastExecution);
                List<SLASchedule> schedules = slaScheduleRepository.findAll();

                List<CalculationResultDto> calculationResultDtos = new ArrayList<>();
                for (SLASchedule slaSchedule : schedules) {
                        List<SLAExecution> slaExecutions = slaExecutionRepository.findByScheduleId(slaSchedule.getId());
                        if (lastExecution) {
                                try {
                                        CalculationResultDto dto = mapDto(slaExecutions.get(slaExecutions.size() - 1));
                                        if (dto != null) {
                                                calculationResultDtos.add(dto);
                                        }
                                } catch (Exception e) {
                                        log.info("LAST_EXECUTION_TRUE ERROR {}", e.getMessage());
                                }

                        } else {
                                for (SLAExecution slaExecution : slaExecutions) {
                                        CalculationResultDto dto = mapDto(slaExecution);
                                        if (dto != null) {
                                                calculationResultDtos.add(dto);
                                        }
                                }
                        }
                }

                return calculationResultDtos;
        }

        public Page<CalculationResultDto> getAllResults(Pageable pageable, Boolean bAudit) throws Exception {

                try {
                        Page<SLAExecution> slaExecutionPage;
                        if (bAudit == null) {
                                slaExecutionPage = calculationResultRepository.findAllResults(pageable);
                        } else {
                                slaExecutionPage = calculationResultRepository.findAllResultsA(pageable, bAudit);

                        }

                        List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                        .stream()
                                        .map(calculation -> mapDto(calculation))
                                        .collect(Collectors.toList());

                        List<CalculationResultDto> calculationResultDtoList = new ArrayList<>();
                        for (int i = 0; i < calculationResultDtos.size(); i++) {
                                if (calculationResultDtos.get(i) == null) {

                                } else {
                                        calculationResultDtoList.add(calculationResultDtos.get(i));
                                }
                        }
                        return new PageImpl<>(calculationResultDtoList, pageable, slaExecutionPage.getTotalElements());
                } catch (Exception ex) {
                        throw new Exception("error in -> " + ex.getMessage());
                }
        }

        public Page<CalculationResultDto> getAllByFiltersWithoutLast(Pageable pageable, String zone, String sla,
                        String type,
                        String startDate, String endDate, Boolean bAudit) {
                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        log.info("if");
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecution(pageable,
                                        zone,
                                        sla, type,
                                        startDate, endDate);
                } else {
                        log.info("else");
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionA(pageable,
                                        zone,
                                        sla, type,
                                        startDate, endDate, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                List<CalculationResultDto> calculationResultDtoList = new ArrayList<>();
                for (int i = 0; i < calculationResultDtos.size(); i++) {
                        if (calculationResultDtos.get(i) == null) {

                        } else {
                                calculationResultDtoList.add(calculationResultDtos.get(i));
                        }
                }

                return new PageImpl<>(calculationResultDtoList, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllByFiltersWithoutDateAndLast(Pageable pageable, String zone, String sla,
                        String type, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionNonDate(
                                        pageable, zone, sla,
                                        type);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionNonDateA(
                                        pageable, zone, sla,
                                        type, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                List<CalculationResultDto> calculationResultDtoList = new ArrayList<>();
                for (int i = 0; i < calculationResultDtos.size(); i++) {
                        if (calculationResultDtos.get(i) == null) {

                        } else {
                                calculationResultDtoList.add(calculationResultDtos.get(i));
                        }
                }

                return new PageImpl<>(calculationResultDtoList, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllFiltersWithoutLastStartDate(Pageable pageable, String zone, String sla,
                        String type,
                        String startDate, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionStartDate(
                                        pageable,
                                        zone,
                                        sla, type, startDate);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionStartDateA(
                                        pageable,
                                        zone,
                                        sla, type, startDate, bAudit);
                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllFiltersWithoutLastEndDate(Pageable pageable, String zone, String sla,
                        String type,
                        String endDate, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionEndDate(
                                        pageable, zone, sla,
                                        type, endDate);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithoutLastExecutionEndDateA(
                                        pageable, zone, sla,
                                        type, endDate, bAudit);
                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllByFiltersWithLast(Pageable pageable, String zone, String sla,
                        String type,
                        String startDate, String endDate, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithLastExecution(pageable, zone, sla,
                                        type,
                                        startDate, endDate);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionA(pageable, zone, sla,
                                        type,
                                        startDate, endDate, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllByFiltersWithLastB(Pageable pageable, String zone, String sla,
                        String type,
                        String startDate, String endDate, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionB(pageable, zone, sla,
                                        type);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionAB(pageable, zone, sla,
                                        type,
                                        startDate, endDate, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllFiltersWithLastStartDate(Pageable pageable, String zone, String sla,
                        String type,
                        String startDate, Boolean bAudit) {
                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionStartDate(
                                        pageable, zone, sla,
                                        type, startDate);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionStartDateA(
                                        pageable, zone, sla,
                                        type, startDate, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllFiltersWithLastEndDate(Pageable pageable, String zone, String sla,
                        String type,
                        String endDate, Boolean bAudit) {

                Page<SLAExecution> slaExecutionPage;
                if (bAudit == null) {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionEndDate(pageable,
                                        zone,
                                        sla,
                                        type, endDate);
                } else {
                        slaExecutionPage = calculationResultRepository.findWithLastExecutionEndDateA(pageable,
                                        zone,
                                        sla,
                                        type, endDate, bAudit);

                }

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public Page<CalculationResultDto> getAllByFiltersWithLastC(Pageable pageable, String sla, String type) {

                Page<SLAExecution> slaExecutionPage = calculationResultRepository.findWithLastExecutionC(pageable, sla,
                                type);

                List<CalculationResultDto> calculationResultDtos = slaExecutionPage
                                .stream()
                                .map(calculation -> mapDto(calculation))
                                .collect(Collectors.toList());

                return new PageImpl<>(calculationResultDtos, pageable, slaExecutionPage.getTotalElements());
        }

        public List<String> getAllZone() {
                LocalDate date = LocalDate.now();
                List<String> allZone = slaDataMeterRepository
                                .getSpatialAvailabilityHesZone(date.plusDays(-2).toString());

                return allZone;
        }

        public List<SLADataMeterDto> getById(long id) {
                Optional<SLAExecution> slaExecution = slaExecutionRepository.findById(id);
                if (slaExecution.isPresent()) {
                        SLAExecutionDto slaExecutionDto = slaExecutionService.mapDto(slaExecution.get());
                        LocalDate slaDate = slaExecutionDto.getSlaDate();
                        String slaName = slaExecutionDto.getSlaName();
                        String slaZone = slaExecutionDto.getZone();
                        SLAScheduleDto scheduleDto = slaExecutionDto.getScheduleId();
                        SLACalculationModuleDto moduleId = scheduleDto.getCalculationMethodsId()
                                        .getCalculationModuleId();

                        Long calculationMethodsId;
                        Integer limitTimesP1;
                        Integer limitTimesP2;
                        Integer limitTimesP3;

                        switch ((int) moduleId.getId()) {
                                case 1:
                                        log.info("-------Configuration of meter for time of use-------");
                                        return slaDataMeterRepository
                                                        .getFailedConfigurationofMeterforTimeofUse(slaDate.toString())
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 2:
                                        log.info("-------Network Availability-------");
                                        return slaDataMeterRepository.getFailedNetworkAvailability(slaDate.toString())
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 3:
                                        log.info("-------Configuration of meter for prepaid--case-3--Failed-SL#27---");
                                        return slaDataMeterRepository
                                                        .getFailedConfigurationOfMeterForPrepaid(
                                                                        "OnDemandSetPaymentMode",
                                                                        "OnDemandGetPaymentMode",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 4:
                                        log.info("-------Remote load control-------");
                                        return slaDataMeterRepository.getFailedRemoteLoadControl(slaDate.toString())
                                                        .stream().collect(Collectors.toList()).stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 5:
                                        log.info("-------Automatic service reduction/interruption---case-5--Failed-SL#30----");
                                        return slaDataMeterRepository
                                                        .getFailedAutomaticServiceReductionInterruption(
                                                                        "OnDemandDisconnect",
                                                                        "OnDemandSetLoadLimitation",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 6:
                                        log.info("-------Remote connect/disconnect-case-6---Failed-KSL#07--");

                                        List<SLADataMeterDto> resp1 = slaDataMeterRepository
                                                        .getFailedRemoteConnectDisconnect("OnDemandDisconnect",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        log.info("qwerty-Failed-KSL#07--> {}", resp1);
                                        return resp1;
                                case 7:
                                        log.info("-------Remote data acquisition-CalculationResultService-Failed--case7---SL#24--");

                                        return slaDataMeterRepository.getFailedRemoteDataAcquisition(
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
                                                        "OnDemandGetTariffAgreement",
                                                        slaDate.toString(), "FINISH_OK").stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 34:
                                        log.info("-------Remote data acquisition - Tolerance--case-34--Failed-KSL#08--");
                                        return slaDataMeterRepository
                                                        .getFailedRemoteDataAcquisitionTolerance(slaDate.toString(),
                                                                        "OnDemandAbsoluteProfile",
                                                                        "OnDemandWaterProfile")
                                                        .stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                        (String) objects[0],
                                                                        (String) objects[1],
                                                                        (String) objects[2],
                                                                        (String) objects[3],
                                                                        (String) objects[4],
                                                                        (String) objects[5],
                                                                        (String) objects[6],
                                                                        ((Timestamp) objects[7]).toInstant()
                                                                                        .atOffset(ZoneOffset.UTC),
                                                                        ((Timestamp) objects[8]).toInstant()
                                                                                        .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 8:
                                case 16:
                                        log.info("------Reconnections from customer care center to energy suppy--case-16--Failed-SL#14--");
                                        return slaDataMeterRepository
                                                        .getFailedReconnectionsFromCustomerCareCenterToEnergySuppy(
                                                                        slaDate.toString(), "OnDemandConnect")
                                                        .stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 17:
                                case 18:
                                case 20:
                                case 23:
                                case 24:
                                case 25:

                                        return Collections.emptyList();
                                case 9:
                                        log.info("-------Alarm in device until message reaches customer--case-9--Failed-SL#13--");
                                        return slaDataMeterRepository
                                                        .getFailedAlarmInDeviceUntilMessageReachesCustomer(
                                                                        slaDate.toString(),
                                                                        "OnAlarmNotification",
                                                                        "OnConnectivityNotification",
                                                                        "OnPowerDownNotification",
                                                                        "OnInstallationNotification")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 10:
                                        log.info("-------Measurement of power indicators--case-10-Failed----");
                                        return slaDataMeterRepository
                                                        .getFailedMeasurementOfPowerIndicators(
                                                                        "OnDemandReadLoadProfile1",
                                                                        "OnDemandReadInstantaneousValues",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                case 11:
                                        log.info("-------Remotely read event logs----case-11--Failed-SL#22---");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> remotelyReadEventP1 = slaDataMeterRepository
                                                        .getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyReadEventP2 = slaDataMeterRepository
                                                        .getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyReadEventP3 = slaDataMeterRepository
                                                        .getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyReadEventB = slaDataMeterRepository
                                                        .getFailedRemotelyReadEventlogsOrReadAFullEventLogOfIndividualMeterB(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyReadEventTotal = new ArrayList<>();

                                        remotelyReadEventTotal.addAll(remotelyReadEventP1);
                                        remotelyReadEventTotal.addAll(remotelyReadEventP2);
                                        remotelyReadEventTotal.addAll(remotelyReadEventP3);
                                        remotelyReadEventTotal.addAll(remotelyReadEventB);

                                        return remotelyReadEventTotal;
                                case 12:
                                        log.info("-------Remotely alter settings in Meter/firmware upgrade---case-12--Failed-SL#21----");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> remotelyAlterSettingsP1 = slaDataMeterRepository
                                                        .getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeA(
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
                                                                        "OnDemandChangeMeterPassword",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyAlterSettingsP2 = slaDataMeterRepository
                                                        .getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeA(
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
                                                                        "OnDemandChangeMeterPassword",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyAlterSettingsP3 = slaDataMeterRepository
                                                        .getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeA(
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
                                                                        "OnDemandChangeMeterPassword",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyAlterSettingsB = slaDataMeterRepository
                                                        .getFailedRemotelyAlterSettingsInMeterFirmwareUpgradeB(
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
                                                                        "OnDemandChangeMeterPassword",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remotelyAlterSettingsTotal = new ArrayList<>();
                                        remotelyAlterSettingsTotal.addAll(remotelyAlterSettingsP1);
                                        remotelyAlterSettingsTotal.addAll(remotelyAlterSettingsP2);
                                        remotelyAlterSettingsTotal.addAll(remotelyAlterSettingsP3);
                                        remotelyAlterSettingsTotal.addAll(remotelyAlterSettingsB);

                                        return remotelyAlterSettingsTotal;

                                case 35:
                                        log.info("-------Max. Transmission delay for switching commands (Bulk data)---case-35--Failed-SL#09----");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> MaxTransDelaySwitchCommandsFailedP1 = slaDataMeterRepository
                                                        .getFailedMaxTransDelaySwitchCommandsA(
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
                                                                        "OnDemandGetTariffAgreement",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelaySwitchCommandsFailedP2 = slaDataMeterRepository
                                                        .getFailedMaxTransDelaySwitchCommandsA(
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
                                                                        "OnDemandGetTariffAgreement",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelaySwitchCommandsFailedP3 = slaDataMeterRepository
                                                        .getFailedMaxTransDelaySwitchCommandsA(
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
                                                                        "OnDemandGetTariffAgreement",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelaySwitchCommandsFailedB = slaDataMeterRepository
                                                        .getFailedMaxTransDelaySwitchCommandsB(
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
                                                                        "OnDemandGetTariffAgreement",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelaySwitchCommandsFailedTotal = new ArrayList<>();
                                        MaxTransDelaySwitchCommandsFailedTotal
                                                        .addAll(MaxTransDelaySwitchCommandsFailedP1);
                                        MaxTransDelaySwitchCommandsFailedTotal
                                                        .addAll(MaxTransDelaySwitchCommandsFailedP2);
                                        MaxTransDelaySwitchCommandsFailedTotal
                                                        .addAll(MaxTransDelaySwitchCommandsFailedP3);
                                        MaxTransDelaySwitchCommandsFailedTotal
                                                        .addAll(MaxTransDelaySwitchCommandsFailedB);

                                        return MaxTransDelaySwitchCommandsFailedTotal;

                                case 36:
                                        log.info("------Max. Transmission delay for updates (Bulk data)---case-36--Failed-SL#10----");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> MaxTransDelayUpdatesP1 = slaDataMeterRepository
                                                        .getFailedMaxTransDelayUpdatesA(
                                                                        "OnDemandFirmwareUpgrade",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelayUpdatesP2 = slaDataMeterRepository
                                                        .getFailedMaxTransDelayUpdatesA(
                                                                        "OnDemandFirmwareUpgrade",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelayUpdatesP3 = slaDataMeterRepository
                                                        .getFailedMaxTransDelayUpdatesA(
                                                                        "OnDemandFirmwareUpgrade",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelayUpdatesB = slaDataMeterRepository
                                                        .getFailedMaxTransDelayUpdatesB(
                                                                        "OnDemandFirmwareUpgrade",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> MaxTransDelayUpdatesTotal = new ArrayList<>();
                                        MaxTransDelayUpdatesTotal.addAll(MaxTransDelayUpdatesP1);
                                        MaxTransDelayUpdatesTotal.addAll(MaxTransDelayUpdatesP2);
                                        MaxTransDelayUpdatesTotal.addAll(MaxTransDelayUpdatesP3);
                                        MaxTransDelayUpdatesTotal.addAll(MaxTransDelayUpdatesB);

                                        return MaxTransDelayUpdatesTotal;

                                case 13:
                                        log.info("-------Meter loss of supply and outage detection---case-13--Failed-SL#20----");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> meterLossOfSupplyAndOutageDetectionP1 = slaDataMeterRepository
                                                        .getFailedMeterLossOfSupplyAndOutageDetectionA(
                                                                        "OnAlarmNotification",
                                                                        "OnConnectivityNotification",
                                                                        "OnPowerDownNotification",
                                                                        "OnInstallationNotification",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> meterLossOfSupplyAndOutageDetectionP2 = slaDataMeterRepository
                                                        .getFailedMeterLossOfSupplyAndOutageDetectionA(
                                                                        "OnAlarmNotification",
                                                                        "OnConnectivityNotification",
                                                                        "OnPowerDownNotification",
                                                                        "OnInstallationNotification",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> meterLossOfSupplyAndOutageDetectionP3 = slaDataMeterRepository
                                                        .getFailedMeterLossOfSupplyAndOutageDetectionA(
                                                                        "OnAlarmNotification",
                                                                        "OnConnectivityNotification",
                                                                        "OnPowerDownNotification",
                                                                        "OnInstallationNotification",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> meterLossOfSupplyAndOutageDetectionB = slaDataMeterRepository
                                                        .getFailedMeterLossOfSupplyAndOutageDetectionB(
                                                                        "OnAlarmNotification",
                                                                        "OnConnectivityNotification",
                                                                        "OnPowerDownNotification",
                                                                        "OnInstallationNotification",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> meterLossOfSupplyAndOutageDetectionTotal = new ArrayList<>();

                                        meterLossOfSupplyAndOutageDetectionTotal
                                                        .addAll(meterLossOfSupplyAndOutageDetectionP1);
                                        meterLossOfSupplyAndOutageDetectionTotal
                                                        .addAll(meterLossOfSupplyAndOutageDetectionP2);
                                        meterLossOfSupplyAndOutageDetectionTotal
                                                        .addAll(meterLossOfSupplyAndOutageDetectionP3);
                                        meterLossOfSupplyAndOutageDetectionTotal
                                                        .addAll(meterLossOfSupplyAndOutageDetectionB);

                                        return meterLossOfSupplyAndOutageDetectionTotal;
                                case 14:
                                        log.info("-------Remote connect/disconnect for selected consumers----case-14--Failed-SL#19---");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> remoteConnectDisconnectForSelectedConsumersP1 = slaDataMeterRepository
                                                        .getFailedRemoteConnectDisconnectForSelectedConsumersA(
                                                                        "OnDemandDisconnect",
                                                                        "OnDemandConnect", slaDate.toString(),
                                                                        "FINISH_OK",
                                                                        "P1", limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remoteConnectDisconnectForSelectedConsumersP2 = slaDataMeterRepository
                                                        .getFailedRemoteConnectDisconnectForSelectedConsumersA(
                                                                        "OnDemandDisconnect",
                                                                        "OnDemandConnect", slaDate.toString(),
                                                                        "FINISH_OK",
                                                                        "P2", limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remoteConnectDisconnectForSelectedConsumersP3 = slaDataMeterRepository
                                                        .getFailedRemoteConnectDisconnectForSelectedConsumersA(
                                                                        "OnDemandDisconnect",
                                                                        "OnDemandConnect", slaDate.toString(),
                                                                        "FINISH_OK",
                                                                        "P3", limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remoteConnectDisconnectForSelectedConsumersB = slaDataMeterRepository
                                                        .getFailedRemoteConnectDisconnectForSelectedConsumersB(
                                                                        "OnDemandDisconnect",
                                                                        "OnDemandConnect", slaDate.toString(),
                                                                        "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> remoteConnectDisconnectForSelectedConsumersTotal = new ArrayList<>();

                                        remoteConnectDisconnectForSelectedConsumersTotal
                                                        .addAll(remoteConnectDisconnectForSelectedConsumersP1);
                                        remoteConnectDisconnectForSelectedConsumersTotal
                                                        .addAll(remoteConnectDisconnectForSelectedConsumersP2);
                                        remoteConnectDisconnectForSelectedConsumersTotal
                                                        .addAll(remoteConnectDisconnectForSelectedConsumersP3);
                                        remoteConnectDisconnectForSelectedConsumersTotal
                                                        .addAll(remoteConnectDisconnectForSelectedConsumersB);

                                        return remoteConnectDisconnectForSelectedConsumersTotal;
                                case 15:
                                        log.info("-------Remote load control commands---case-15--Failed-SL#18------");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> rP1 = slaDataMeterRepository
                                                        .getFailedRemoteLoadControlCommandsA(
                                                                        "OnDemandSetLoadLimitation",
                                                                        "OnDemandGetLoadLimitThreshold",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                        List<SLADataMeterDto> rP2 = slaDataMeterRepository
                                                        .getFailedRemoteLoadControlCommandsA(
                                                                        "OnDemandSetLoadLimitation",
                                                                        "OnDemandGetLoadLimitThreshold",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                        List<SLADataMeterDto> rP3 = slaDataMeterRepository
                                                        .getFailedRemoteLoadControlCommandsA(
                                                                        "OnDemandSetLoadLimitation",
                                                                        "OnDemandGetLoadLimitThreshold",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> rB1 = slaDataMeterRepository
                                                        .getFailedRemoteLoadControlCommandsB(
                                                                        "OnDemandSetLoadLimitation",
                                                                        "OnDemandGetLoadLimitThreshold",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> rTotal = new ArrayList<>();
                                        rTotal.addAll(rP1);
                                        rTotal.addAll(rP2);
                                        rTotal.addAll(rP3);
                                        rTotal.addAll(rB1);

                                        return rTotal;

                                case 19:
                                        log.info("-------New connection-------");
                                        break;
                                case 21:
                                        log.info("-------Network availability - HES-------");
                                        return slaDataMeterRepository.getFailedNetworkavailibityHES(slaDate.toString())
                                                        .stream().collect(Collectors.toList()).stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                objects[7] == null ? null
                                                                                : ((Timestamp) objects[7])
                                                                                                .toInstant()
                                                                                                .atOffset(ZoneOffset.UTC),
                                                                objects[8] == null ? null
                                                                                : ((Timestamp) objects[8])
                                                                                                .toInstant()
                                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 22:
                                        log.info("-------Read the event logs pertaining to all Meters----case-22--Failed-SL#23---");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> readTheEventLogsPertainingToAllMetersP1 = slaDataMeterRepository
                                                        .getFailedReadTheEventLogsPertainingToAllMetersA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> readTheEventLogsPertainingToAllMetersP2 = slaDataMeterRepository
                                                        .getFailedReadTheEventLogsPertainingToAllMetersA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> readTheEventLogsPertainingToAllMetersP3 = slaDataMeterRepository
                                                        .getFailedReadTheEventLogsPertainingToAllMetersA(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> readTheEventLogsPertainingToAllMetersB = slaDataMeterRepository
                                                        .getFailedReadTheEventLogsPertainingToAllMetersB(
                                                                        "OnDemandEventLog",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> readTheEventLogsPertainingToAllMetersTotal = new ArrayList<>();
                                        readTheEventLogsPertainingToAllMetersTotal
                                                        .addAll(readTheEventLogsPertainingToAllMetersP1);
                                        readTheEventLogsPertainingToAllMetersTotal
                                                        .addAll(readTheEventLogsPertainingToAllMetersP2);
                                        readTheEventLogsPertainingToAllMetersTotal
                                                        .addAll(readTheEventLogsPertainingToAllMetersP3);
                                        readTheEventLogsPertainingToAllMetersTotal
                                                        .addAll(readTheEventLogsPertainingToAllMetersB);

                                        return readTheEventLogsPertainingToAllMetersTotal;
                                case 26:
                                        log.info("-------Meter alarms/logged events-------");
                                        return slaDataMeterRepository
                                                        .getFailedMeterAlarmsLoggedEvents(slaDate.toString()).stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 27:
                                        log.info("-------Collection of daily Meter interval reading - HES-------");
                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingHES1 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingHES1(
                                                                        slaDate.toString())
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                        //List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingHES2 = slaDataMeterRepository
                                        //                .getFailedCollectionOfDailyMeterIntervalReadingHES2(
                                        //                                slaDate.toString())
                                        //                .stream().map(objects -> new SLADataMeterDto(
                                        //                        (String) objects[0],
                                        //                        (String) objects[1],
                                        //                        (String) objects[2],
                                        //                        (String) objects[3],
                                        //                       (String) objects[4],
                                        //                        (String) objects[5],
                                        //                        (String) objects[6],
                                        //                        objects[7] == null ? null
                                        //                                        : ((Timestamp) objects[7])
                                        //                                                        .toInstant()
                                        //                                                        .atOffset(ZoneOffset.UTC),
                                        //                        objects[8] == null ? null
                                        //                                        : ((Timestamp) objects[8])
                                        //                                                        .toInstant()
                                        //                                                        .atOffset(ZoneOffset.UTC)))
                                        //                .collect(Collectors.toList());
                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingHES3 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingHES3(
                                                                        slaDate.toString())
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                objects[7] == null ? null
                                                                                : ((Timestamp) objects[7])
                                                                                                .toInstant()
                                                                                                .atOffset(ZoneOffset.UTC),
                                                                objects[8] == null ? null
                                                                                : ((Timestamp) objects[8])
                                                                                                .toInstant()
                                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingHESTotal = new ArrayList<>(
                                                        collectionOfDailyMeterIntervalReadingHES1);
                                        //collectionOfDailyMeterIntervalReadingHESTotal.addAll(collectionOfDailyMeterIntervalReadingHES2);
                                        collectionOfDailyMeterIntervalReadingHESTotal
                                                        .addAll(collectionOfDailyMeterIntervalReadingHES3);
                                        return collectionOfDailyMeterIntervalReadingHESTotal;
                                case 28:
                                        log.info("-------Individual read - HES--case-28--Failed--SL#17---");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> failedIndividualReadHESP1 = slaDataMeterRepository
                                                        .getFailedIndividualReadHESA("LoadProfile1", slaDate.toString(),
                                                                        "FINISH_OK", "P1", limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> failedIndividualReadHESP2 = slaDataMeterRepository
                                                        .getFailedIndividualReadHESA("LoadProfile1", slaDate.toString(),
                                                                        "FINISH_OK", "P2", limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> failedIndividualReadHESP3 = slaDataMeterRepository
                                                        .getFailedIndividualReadHESA("LoadProfile1", slaDate.toString(),
                                                                        "FINISH_OK", "P3", limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> failedIndividualReadHESB = slaDataMeterRepository
                                                        .getFailedIndividualReadHESB("LoadProfile1", slaDate.toString(),
                                                                        "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> failedIndividualReadHESTotal = new ArrayList<>();
                                        failedIndividualReadHESTotal.addAll(failedIndividualReadHESP1);
                                        failedIndividualReadHESTotal.addAll(failedIndividualReadHESP2);
                                        failedIndividualReadHESTotal.addAll(failedIndividualReadHESP3);
                                        failedIndividualReadHESTotal.addAll(failedIndividualReadHESB);

                                        return failedIndividualReadHESTotal;
                                case 29:
                                        log.info("-------Spatial availability - HES-------");
                                        log.info("-> {}", slaZone);
                                        String zone = (slaZone == null) ? " " : slaZone.toString();
                                        return slaDataMeterRepository
                                                        .getFailedSpatialAvailabilityHES(zone, slaDate.toString())
                                                        .stream().collect(Collectors.toList()).stream()
                                                        .map(objects -> new SLADataMeterDto(
                                                                        (String) objects[0],
                                                                        (String) objects[1],
                                                                        (String) objects[2],
                                                                        (String) objects[3],
                                                                        (String) objects[4],
                                                                        (String) objects[5],
                                                                        (String) objects[6],
                                                                        objects[7] == null ? null
                                                                                        : ((Timestamp) objects[7])
                                                                                                        .toInstant()
                                                                                                        .atOffset(ZoneOffset.UTC),
                                                                        objects[8] == null ? null
                                                                                        : ((Timestamp) objects[8])
                                                                                                        .toInstant()
                                                                                                        .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                case 30:
                                        log.info("-------Collection of daily Meter interval reading - Tolerance 1---case-30--Failed-KSL#06--");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance1P1 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance1P2 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance1P3 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        // segun jira, Se usan los mismos comandos viejos
                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance1B = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceB(
                                                                        "EnergyProfile", "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingToleranceTotal1 = new ArrayList<>();
                                        collectionOfDailyMeterIntervalReadingToleranceTotal1
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance1P1);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal1
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance1P2);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal1
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance1P3);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal1
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance1B);

                                        return collectionOfDailyMeterIntervalReadingToleranceTotal1;
                                case 31:
                                        log.info("-------Collection of daily Meter interval reading - Tolerance 2---case-31--Failed-KSL#06----");

                                        calculationMethodsId = slaCalculationMethodsRepository
                                                        .slaCalculationMethods(slaName).getId();
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

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance2P1 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P1",
                                                                        limitTimesP1)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance2P2 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P2",
                                                                        limitTimesP2)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance2P3 = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceA(
                                                                        "EnergyProfile",
                                                                        "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK", "P3",
                                                                        limitTimesP3)
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        // segun jira, Se usan los mismos comandos viejos
                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingTolerance2B = slaDataMeterRepository
                                                        .getFailedCollectionOfDailyMeterIntervalReadingToleranceB(
                                                                        "EnergyProfile", "WaterProfile",
                                                                        slaDate.toString(), "FINISH_OK")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());

                                        List<SLADataMeterDto> collectionOfDailyMeterIntervalReadingToleranceTotal2 = new ArrayList<>();
                                        collectionOfDailyMeterIntervalReadingToleranceTotal2
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance2P1);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal2
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance2P2);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal2
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance2P3);
                                        collectionOfDailyMeterIntervalReadingToleranceTotal2
                                                        .addAll(collectionOfDailyMeterIntervalReadingTolerance2B);

                                        return collectionOfDailyMeterIntervalReadingToleranceTotal2;

                                case 33:
                                        log.info("-------Reconnections from customer care center to energy supply----Failed-KSL#05--");
                                        return slaDataMeterRepository
                                                        .getFailedReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(
                                                                        slaDate.toString(),
                                                                        "OnDemandDisconnect")
                                                        .stream().map(objects -> new SLADataMeterDto(
                                                                (String) objects[0],
                                                                (String) objects[1],
                                                                (String) objects[2],
                                                                (String) objects[3],
                                                                (String) objects[4],
                                                                (String) objects[5],
                                                                (String) objects[6],
                                                                ((Timestamp) objects[7]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC),
                                                                ((Timestamp) objects[8]).toInstant()
                                                                                .atOffset(ZoneOffset.UTC)))
                                                        .collect(Collectors.toList());
                                default:
                                        log.info("-------Unknown moduleId-------");
                                        return Collections.emptyList();
                        }
                }
                return Collections.emptyList();
        }

        public CalculationResultDto mapDto(SLAExecution slaExecution) {
                CalculationResultDto dto = new CalculationResultDto();
                SLAExecutionDto slaExecutionDto = slaExecutionService.mapDto(slaExecution);
                if (slaExecution.getStatus().equals("done")) {
                        dto.setSlaExecutionDto(slaExecutionDto);
                        return dto;
                }

                return null;
        }

        public String escapeSpecialCharacters(String data) {
                if (data == null) {
                        throw new IllegalArgumentException("Input data cannot be null");
                }
                String escapedData = data.replaceAll("\\R", " ");
                if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                        data = data.replace("\"", "\"\"");
                        escapedData = "\"" + data + "\"";
                }
                return escapedData;
        }

        public String getAudit(List<Object[]> data, Long id, String slaName)
                        throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
                String CSV_FILE_NAME = "auditfile_" + slaName + "_" + id.toString() + ".csv";
                String ZIP_FILE_NAME = "auditfile_" + slaName + "_" + id.toString() + ".zip";
                String CSV_FILE_PATH = "/audit/";
                File dir = new File(CSV_FILE_PATH);
                dir.mkdirs();
                try {

                        List<SLAAuditDtoS> mappedData = new ArrayList<>();
                        SLAAuditDtoS headers = new SLAAuditDtoS();
                        headers.setProcessDate("processDate");
                        headers.setPriority("Priority");
                        headers.setOrganization("organization");
                        headers.setSerialNumber("serialNumber");
                        headers.setStatus("status");
                        headers.setZone("zone");
                        headers.setDatetime("meterType");
                        headers.setOrderName("orderName");
                        headers.setOrderStatus("orderStatus");
                        headers.setDatetime("datetime");
                        headers.setInittime("inittime");
                        headers.setFinishtime("finishtime");
                        mappedData.add(headers);
                        if (data != null) {
                                for (Object[] row : data) {
                                        SLAAuditDtoS dto = new SLAAuditDtoS();
                                        dto.setProcessDate(row[0] != null ? (String) row[0].toString() : "");
                                        dto.setPriority(row[1] != null ? (String) row[1] : "");
                                        dto.setOrganization(row[2] != null ? (String) row[2] : "");
                                        dto.setSerialNumber(row[3] != null ? (String) row[3] : "");
                                        dto.setStatus(row[4] != null ? (String) row[4] : "");
                                        dto.setZone(row[5] != null ? (String) row[5] : "");
                                        dto.setMeterType(row[6] != null ? (String) row[6] : "");
                                        dto.setOrderName(row[7] != null ? (String) row[7] : "");
                                        dto.setOrderStatus(row[8] != null ? (String) row[8] : "");
                                        dto.setDatetime(row[9] != null ? (String) row[9].toString() : "");
                                        dto.setInittime(row[10] != null ? (String) row[10].toString() : "");
                                        dto.setFinishtime(row[11] != null ? (String) row[11].toString() : "");
                                        mappedData.add(dto);
                                }
                        }
                        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                                        new FileOutputStream(CSV_FILE_PATH + ZIP_FILE_NAME))) {
                                ZipEntry zipEntry = new ZipEntry(CSV_FILE_NAME);
                                zipOutputStream.putNextEntry(zipEntry);
                                for (SLAAuditDtoS row : mappedData) {
                                        String csvRow = convertToCSV(row);
                                        zipOutputStream.write(csvRow.getBytes());
                                        zipOutputStream.write("\n".getBytes());
                                }
                                zipOutputStream.closeEntry();

                        } catch (Exception e) {
                        }

                } catch (Exception e) {
                }

                String fileUrl = CSV_FILE_PATH + ZIP_FILE_NAME;
                return fileUrl;
        }
        
        public String getAudit(Stream<Object[]> data, Long id, String slaName)
                throws IOException {
            String CSV_FILE_NAME = "auditfile_" + slaName + "_" + id.toString() + ".csv";
            String ZIP_FILE_NAME = "auditfile_" + slaName + "_" + id.toString() + ".zip";
            String CSV_FILE_PATH = "/audit/";
            File dir = new File(CSV_FILE_PATH);
            dir.mkdirs();
            
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                    new FileOutputStream(CSV_FILE_PATH + ZIP_FILE_NAME))) {
                ZipEntry zipEntry = new ZipEntry(CSV_FILE_NAME);
                zipOutputStream.putNextEntry(zipEntry);

                // Escribir encabezados CSV
                String headers = "processDate,Priority,organization,serialNumber,status,zone,meterType,orderName,orderStatus,datetime,inittime,finishtime\n";
                zipOutputStream.write(headers.getBytes(StandardCharsets.UTF_8));

                // Convertir y escribir datos de cada fila del Stream a CSV
                data.forEach(row -> {
                    try {
                        String csvRow = convertRowToCSV(row);
                        zipOutputStream.write(csvRow.getBytes(StandardCharsets.UTF_8));
                        zipOutputStream.write("\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return CSV_FILE_PATH + ZIP_FILE_NAME;
        }

        private String convertRowToCSV(Object[] row) {
            StringBuilder csvRow = new StringBuilder();
            for (Object column : row) {
                csvRow.append(column != null ? column.toString() : "").append(",");
            }
            return csvRow.toString();
        }
        
        public String convertToCSV(SLAAuditDtoS data) {
                String[] dataArray = new String[] {
                                data.getProcessDate(),
                                data.getPriority(),
                                data.getOrganization(),
                                data.getSerialNumber(),
                                data.getStatus(),
                                data.getZone(),
                                data.getDatetime(),
                                data.getOrderName(),
                                data.getOrderStatus(),
                                data.getInittime(),
                                data.getFinishtime()
                };
                return Stream.of(dataArray)
                                .map(this::escapeSpecialCharacters)
                                .collect(Collectors.joining(","));
        }

        public void getReport(long id, long moduleId) throws SQLException, ClassNotFoundException {
                Connection conn = null;

                Class.forName(driverClassName);
                conn = DriverManager.getConnection(databaseAddress, username, password);

                if (Objects.nonNull(conn)) {
                        log.info("The connection is success -> {}", true);
                } else {
                        log.info("Sorry, error for the credential -> {}", false);
                }

                Optional<SLAExecution> getExecution = slaExecutionRepository.findById(id);

                long scheduleId = getExecution.get().getScheduleId();

                List<SLAExecution> slaExecutions = slaExecutionRepository.findByScheduleId(scheduleId);

                if (getExecution.get().getAudit()) {
                        for (SLAExecution slaExecution : slaExecutions) {
                                if (slaExecution.getStatus().contains("pending")
                                                || slaExecution.getStatus().contains("error")) {

                                        slaExecution.setStatus("done");
                                        slaExecution.setExecutionResult("ok");
                                        slaExecutionRepository.save(slaExecution);
                                }
                        }
                        String date = String.valueOf(getExecution.get().getSlaDate());

                        try {

                                if (moduleId == 2) {
                                        PreparedStatement stmt = conn
                                                        .prepareStatement("select * from sla_meter_staging ms " +
                                                                        "where ms.status='ACTIVE'" +
                                                                        "and ms.process_date=CAST(? AS Date) " +
                                                                        "and ms.priority in ('P1', 'P2', 'P3')");
                                        stmt.setString(1, date);

                                        ResultSet rs = stmt.executeQuery();

                                        FileWriter fileWriter = new FileWriter("/home/slauser/slatool-api/csv-files/"
                                                        + getExecution.get().getSlaName() + "-"
                                                        + getExecution.get().getSlaDate() + ".csv");

                                        CSVWriter csvWriter = new CSVWriter(fileWriter);

                                        csvWriter.writeNext(new String[] {
                                                        "Priority", "Serial_Number", "Zone",
                                                        "Process_Date", "Organization", "Status",
                                                        "Meter_Type"
                                        });

                                        while (rs.next()) {
                                                String priority = rs.getString("priority");
                                                String serial_number = rs.getString("serial_number");
                                                String zone = rs.getString("zone");
                                                String process_date = rs.getString("process_date");
                                                String organization = rs.getString("organization");
                                                String status = rs.getString("status");
                                                String meter_type = rs.getString("meter_type");

                                                csvWriter.writeNext(new String[] {
                                                                priority, serial_number, zone,
                                                                process_date, organization, status,
                                                                meter_type
                                                });
                                        }
                                }

                                if (!conn.isClosed()) {
                                }
                        } catch (Exception e) {
                                System.err.println("Exception:" + e.getMessage());
                        } finally {

                                try {
                                        if (conn != null) {
                                                conn.close();
                                        }
                                } catch (SQLException e) {
                                        System.err.println("SQL Exception:" + e.getMessage());
                                }

                        }

                } else {
                        for (SLAExecution slaExecution : slaExecutions) {
                                if (slaExecution.getStatus().contains("done")) {
                                        slaExecution.setAudit(false);
                                        slaExecutionRepository.save(slaExecution);
                                }
                        }
                }
        }
        
        public void zipDirectory(String sourceFolder, String outputFile) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                File folder = new File(sourceFolder);
                byte[] buffer = new byte[1024];
                for (File file : folder.listFiles()) {
                    if (file.isFile()) {
                    	// Agregar entrada zip para el archivo
                        ZipEntry entrada = new ZipEntry(file.getName());
                        zos.putNextEntry(entrada);

                        // Leer el contenido del archivo y escribirlo en el archivo comprimido
                        FileInputStream fis = new FileInputStream(file);
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        // Cerrar el flujo de entrada del archivo
                        fis.close();
                    }
                }
                zos.closeEntry();
                zos.close();
            }
        }
}
