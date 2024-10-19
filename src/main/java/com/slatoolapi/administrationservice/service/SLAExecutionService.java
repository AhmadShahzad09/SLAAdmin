package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.*;
import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLACalculationModule;
import com.slatoolapi.administrationservice.entity.SLAExecution;
import com.slatoolapi.administrationservice.entity.SLASchedule;
import com.slatoolapi.administrationservice.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
@EnableScheduling
@Slf4j
public class SLAExecutionService {

        @Value("${spring.application.timezone}")
        private static final String TIME_ZONE = "UTC";

        @Autowired
        private SLAExecutionRepository slaExecutionRepository;

        @Autowired
        private SLACalculationMethodsRepository slaCalculationMethodsRepository;

        @Autowired
        private SLACalculationModuleRepository slaCalculationModuleRepository;

        @Autowired
        private SLAScheduleRepository slaScheduleRepository;

        @Autowired
        private SLAScheduleService slaScheduleService;

        @Autowired
        private SLADataMeterRepository slaDataMeterRepository;

        @Autowired
        private SLAAsyncExecutionService slaAsyncExecutionService;

        @Scheduled(cron = "0 */5 * * * *", zone = TIME_ZONE)
        public void autoExecution() throws SQLException, ClassNotFoundException {

                Date date = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                List<SLASchedule> schedules = slaScheduleRepository.findAll();

                if (schedules == null || schedules.size() == 0 || schedules.isEmpty()) {
                } else {
                        Integer cq = 0;
                        Integer cp = 0;
                        for (SLASchedule schedule : schedules) {
                                cq++;
                                if (schedule.getExecutionTime() == hour) {
                                        cp++;

                                        slaAsyncExecutionService.execution(schedule.getId());
                                }
                        }
                }
        }

        @Scheduled(cron = "0 0 0 * * *", zone = TIME_ZONE)
        public void autoCreateExecution() {
                List<SLASchedule> schedules = slaScheduleRepository.findAll();
                if (schedules.size() == 0) {
                }
                for (SLASchedule schedule : schedules) {
                        create(schedule.getId());
                }
        }

        public void create(long scheduleId) {
                try {
                        SLASchedule slaSchedule = slaScheduleRepository.findById(scheduleId).orElse(null);
                        if (slaSchedule == null) {
                                return;
                        }

                        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                                        .findById(slaSchedule.getCalculationMethodsId()).orElse(null);
                        if (slaCalculationMethods == null) {
                                return;
                        }

                        SLACalculationModule slaCalculationModule = slaCalculationModuleRepository
                                        .findById(slaCalculationMethods.getCalculationModuleId()).orElse(null);
                        if (slaCalculationModule == null) {
                                return;
                        }
                        LocalDate now = LocalDate.now();
                        Period period = Period.ofDays(-1);
                        LocalDate nowSubstringTwo = LocalDate.now();
                        Period periodTwo = Period.ofDays(-2);
                        LocalDateTime dateTime = LocalDateTime.now();

                        String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String timeFormat = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                        String[] date = dateFormat.split("-");
                        String[] time = timeFormat.split(":");

                        int year = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int day = Integer.parseInt(date[2]);
                        int hour = Integer.parseInt(time[0]);
                        int minute = Integer.parseInt(time[1]);

                        LocalDateTime getDtExtract = LocalDateTime.of(year, month, day, hour, minute);

                        String getDtExtractDate = getDtExtract
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss",
                                        Locale.ENGLISH);

                        LocalDateTime dtExtract = LocalDateTime.parse(getDtExtractDate, inputFormatter);

                        LocalDate convertDateFinal = LocalDate.of(year, month, day);

                        long calculationModule = slaCalculationModule.getId();

                        if (calculationModule == 29) {

                                if (!slaDataMeterRepository.getSpatialAvailabilityHesZone(
                                                now.plus(period).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                                .isEmpty()) {
                                        List<String> zoneList = slaDataMeterRepository.getSpatialAvailabilityHesZone(
                                                        now.plus(period).format(
                                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                        List<SLAExecution> slaExecutions1 = new ArrayList<>();
                                        int x = 0;
                                        for (String zone : zoneList) {
                                                SLAExecution slaExecutionBuild = SLAExecution.builder()
                                                                .scheduleId(slaSchedule.getId())
                                                                .slaName(slaCalculationMethods.getName())
                                                                .type(slaCalculationMethods.getSlaType())
                                                                .description(slaCalculationMethods.getDescription())
                                                                .status("pending")
                                                                .slaDate(convertDateFinal).startDateTime(dtExtract)
                                                                .endDateTime(dtExtract).zone(zone)
                                                                .executionResult(""). // before "Not ok"
                                                                audit(false). // slaExecutionPostDto.getIsAudit()
                                                                build();
                                                SLAExecution slaExecutionNew = slaExecutionRepository
                                                                .save(slaExecutionBuild);
                                                if (zoneList.size() == x) {
                                                        mapDto(slaExecutionNew);
                                                }
                                                ++x;
                                        }
                                }
                        } else if (slaCalculationModule.getId() == 27 ||
                                        slaCalculationModule.getId() == 28 ||
                                        slaCalculationModule.getId() == 30 ||
                                        slaCalculationModule.getId() == 31) {

                                SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                                .slaName(slaCalculationMethods.getName())
                                                .type(slaCalculationMethods.getSlaType())
                                                .description(slaCalculationMethods.getDescription()).status("pending")
                                                .slaDate(nowSubstringTwo.plus(periodTwo)).startDateTime(dtExtract)
                                                .endDateTime(dtExtract)
                                                .executionResult("").audit(false).build();
                                SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);

                                mapDto(slaExecutionNew);

                        } else {
                                SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                                .slaName(slaCalculationMethods.getName())
                                                .type(slaCalculationMethods.getSlaType())
                                                .description(slaCalculationMethods.getDescription()).status("pending")
                                                .slaDate(now.plus(period))
                                                .startDateTime(dtExtract).endDateTime(dtExtract).executionResult("")
                                                .audit(false).build();
                                SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);

                                mapDto(slaExecutionNew);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public SLAExecutionDto save(SLAExecutionPostDto slaExecutionPostDto) {

                SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                                .findById(slaExecutionPostDto.getCalculationMethodsId()).orElse(null);
                if (slaCalculationMethods == null) {
                        return null;
                }

                SLASchedule slaSchedule = slaScheduleRepository
                                .findByCalculationMethodsId(slaExecutionPostDto.getCalculationMethodsId()).orElse(null);
                if (slaSchedule == null) {
                        return null;
                }

                String getTime = slaExecutionPostDto.getTime();
                String[] parts = getTime.split(":");
                int num = 0;

                if (Objects.equals(parts[0], "0")) {
                        num = Integer.parseInt(parts[0].substring(1));
                } else {
                        String concatTime = parts[0];
                        num = Integer.parseInt(concatTime);
                }

                slaSchedule.setExecutionTime(num);
                slaScheduleRepository.save(slaSchedule);
                LocalDate now = LocalDate.now();
                Period period = Period.ofDays(-1);

                LocalDateTime dateTime = LocalDateTime.now();

                String slaDate = slaExecutionPostDto.getDate();
                String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String timeFormat = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                String[] date = dateFormat.split("-");
                String[] time = timeFormat.split(":");

                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                LocalDateTime getDtExtract = LocalDateTime.of(year, month, day, hour, minute);

                String getDtExtractDate = getDtExtract.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

                LocalDateTime dtExtract = LocalDateTime.parse(getDtExtractDate, inputFormatter);
                String[] partsDate = slaDate.split("-");
                int yearDate = Integer.parseInt(partsDate[0]);
                int monthDate = Integer.parseInt(partsDate[1]);
                int dayDate = Integer.parseInt(partsDate[2].substring(0, 2));

                LocalDate convertDateFinal = LocalDate.of(yearDate, monthDate, dayDate);

                SLACalculationMethods slaCalculation = slaCalculationMethodsRepository
                                .findById(slaExecutionPostDto.getCalculationMethodsId()).orElse(null);

                assert slaCalculation != null;

                if (slaCalculation.getCalculationModuleId() == 29) {
                        if (!slaDataMeterRepository
                                        .getSpatialAvailabilityHesZone(convertDateFinal
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                        .isEmpty()) {
                                List<String> zoneList = slaDataMeterRepository.getSpatialAvailabilityHesZone(
                                                convertDateFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                List<SLAExecution> slaExecutions1 = new ArrayList<>();
                                int x = 0;
                                for (String zone : zoneList) {
                                        ++x;
                                        SLAExecution slaExecutionBuild = SLAExecution.builder()
                                                        .scheduleId(slaSchedule.getId())
                                                        .slaName(slaCalculationMethods.getName())
                                                        .type(slaCalculationMethods.getSlaType())
                                                        .email(slaExecutionPostDto.getEmail())
                                                        .description(slaCalculationMethods.getDescription())
                                                        .status("pending")
                                                        .slaDate(convertDateFinal).startDateTime(dtExtract)
                                                        .endDateTime(dtExtract).zone(zone)
                                                        .executionResult("").audit(slaExecutionPostDto.getIsAudit())
                                                        .build();
                                        SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);
                                        if (zoneList.size() == x) {
                                                return mapDto(slaExecutionNew);
                                        }
                                }
                        } else {
                                SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                                .slaName(slaCalculationMethods.getName())
                                                .type(slaCalculationMethods.getSlaType())
                                                .description(slaCalculationMethods.getDescription()).status("error")
                                                .slaDate(convertDateFinal)

                                                .email(slaExecutionPostDto.getEmail())
                                                .startDateTime(dtExtract).endDateTime(dtExtract)
                                                .executionResult("Not ok").audit(slaExecutionPostDto.getIsAudit())
                                                .build();
                                SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);
                                return mapDto(slaExecutionNew);
                        }
                }

                if (slaExecutionPostDto.getEmail() == null) {

                        SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                        .slaName(slaCalculationMethods.getName())
                                        .type(slaCalculationMethods.getSlaType())
                                        .description(slaCalculationMethods.getDescription())
                                        .status("pending")
                                        .slaDate(convertDateFinal)
                                        .startDateTime(dtExtract).endDateTime(dtExtract)
                                        .executionResult("")
                                        .audit(slaExecutionPostDto.getIsAudit())
                                        .build();
                        SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);
                        return mapDto(slaExecutionNew);

                } else {
                        SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                        .slaName(slaCalculationMethods.getName())
                                        .type(slaCalculationMethods.getSlaType())
                                        .description(slaCalculationMethods.getDescription())
                                        .status("pending")
                                        .slaDate(convertDateFinal)
                                        .startDateTime(dtExtract).endDateTime(dtExtract)
                                        .executionResult("")
                                        .audit(slaExecutionPostDto.getIsAudit())
                                        .email(slaExecutionPostDto.getEmail())
                                        .build();
                        SLAExecution slaExecutionNew = slaExecutionRepository.save(slaExecutionBuild);
                        return mapDto(slaExecutionNew);
                }

        }

        public Page<SLAExecutionDto> getAllFiltersTest(String slaName, String status, String result, String slaDate,
                        String startDate, String endDate, String email,
                        Pageable pageable, Boolean bAudit) {
                Page<SLAExecution> slaExecutions = slaExecutionRepository.findAllFiltersTesting(slaName, status, result,
                                slaDate, startDate, endDate, email, pageable, bAudit);

                List<SLAExecutionDto> slaExecutionDtoList = slaExecutions
                                .stream()
                                .map(slaExecution -> mapDto(slaExecution))
                                .collect(Collectors.toList());
                return new PageImpl<>(slaExecutionDtoList, pageable, slaExecutions.getTotalElements());
        }

        public Page<SLAExecutionDto> getAllfilterTestMonthly(String slaName, String status, String result,
                        String slaDate,
                        String startDate, String endDate,
                        String email,
                        Pageable pageable, Boolean bAudit) {
                Page<SLAExecution> slaExecutions = slaExecutionRepository.findAllFiltersTestingMonthly(slaName, status,
                                result,
                                slaDate, startDate, endDate, email, pageable, bAudit);
                List<SLAExecutionDto> slaExecutionDtoList = slaExecutions
                                .stream()
                                .map(slaExecution -> mapDto(slaExecution))
                                .collect(Collectors.toList());

                return new PageImpl<>(slaExecutionDtoList, pageable, slaExecutions.getTotalElements());
        }

        public Page<SLAExecutionDto> getLastMonthly(Pageable pageable, Boolean bAudit) {

                Page<SLAExecution> slaExecutions = slaExecutionRepository.findLastMonthly(pageable, bAudit);
                List<SLAExecutionDto> slaExecutionDtoList = slaExecutions
                                .stream()
                                .map(slaExecution -> mapDto(slaExecution))
                                .collect(Collectors.toList());
                return new PageImpl<>(slaExecutionDtoList, pageable, slaExecutions.getTotalElements());
        }

        public Page<SLAExecutionDto> getAllExecution(Pageable pageable, Boolean bAudit) {

                Page<SLAExecution> slaExecutions = slaExecutionRepository.findAllWA(pageable, bAudit);

                List<SLAExecutionDto> slaExecutionDtoList = slaExecutions
                                .stream()
                                .map(slaExecution -> mapDto(slaExecution))
                                .collect(Collectors.toList());
                return new PageImpl<>(slaExecutionDtoList, pageable, slaExecutions.getTotalElements());
        }

        public SLAExecutionDto getById(long id) {
                SLAExecution slaExecution = slaExecutionRepository.findById(id).orElse(null);
                if (slaExecution != null) {
                        return mapDto(slaExecution);
                }
                return null;
        }

        public SLAExecutionDto update(long id, SLAExecution slaExecution) {
                SLAExecution slaExecutionUpdated = slaExecutionRepository.findById(id).orElse(null);
                if (slaExecutionUpdated == null) {
                        return null;
                }

                SLASchedule slaSchedule = slaScheduleRepository.findById(slaExecution.getScheduleId()).orElse(null);
                if (slaSchedule == null) {
                        return null;
                }

                slaExecutionUpdated.setScheduleId(slaExecution.getScheduleId());
                slaExecutionUpdated.setSlaName(slaExecution.getSlaName());
                slaExecutionUpdated.setStatus(slaExecution.getStatus());
                slaExecutionUpdated.setSlaDate(slaExecution.getSlaDate());
                slaExecutionUpdated.setType(slaExecution.getType());
                slaExecutionUpdated.setStatus(slaExecution.getStatus()); // revisarlo
                slaExecutionUpdated.setStartDateTime(slaExecution.getStartDateTime());
                slaExecutionUpdated.setEndDateTime(slaExecution.getEndDateTime());
                slaExecutionUpdated.setExecutionResult(slaExecution.getExecutionResult());
                slaExecutionUpdated.setOverallAchieved(slaExecutionUpdated.getOverallAchieved());
                slaExecutionUpdated.setOverallMetersAffected(slaExecution.getOverallMetersAffected());
                slaExecutionUpdated.setP1Achieved(slaExecution.getP1Achieved());
                slaExecutionUpdated.setP1MetersAffected(slaExecution.getP1MetersAffected());
                slaExecutionUpdated.setP2Achieved(slaExecution.getP2Achieved());
                slaExecutionUpdated.setP2MetersAffected(slaExecution.getP2MetersAffected());
                slaExecutionUpdated.setP3Achieved(slaExecution.getP3Achieved());
                slaExecutionUpdated.setP3MetersAffected(slaExecution.getP3MetersAffected());
                slaExecutionUpdated.setAudit(slaExecution.getAudit());

                slaExecutionRepository.save(slaExecutionUpdated);

                return mapDto(slaExecutionUpdated);
        }

        public boolean delete(long id) {
                SLAExecution slaExecution = slaExecutionRepository.findById(id).orElse(null);
                if (slaExecution != null) {
                        slaExecutionRepository.deleteById(id);
                        return true;
                }
                return false;

        }

        public SLAExecutionDto mapDto(SLAExecution slaExecution) {

                SLASchedule slaSchedule = slaScheduleRepository.findById(slaExecution.getScheduleId()).orElse(null);
                if (slaSchedule == null) {
                        return null;
                }
                SLAScheduleDto slaScheduleDto = slaScheduleService.mapDto(slaSchedule);

                SLACalculationMethods calculationMethods = slaCalculationMethodsRepository
                                .slaCalculationMethods(slaExecution.getSlaName());

                SLAExecutionDto dto = new SLAExecutionDto();
                dto.setId(slaExecution.getId());
                dto.setScheduleId(slaScheduleDto);
                dto.setSlaName(slaExecution.getSlaName());
                dto.setStatus(slaExecution.getStatus());
                dto.setSlaDate(slaExecution.getSlaDate());
                if (slaExecution.getEmail() != null) {
                        dto.setEmail(slaExecution.getEmail());
                }
                dto.setDescription(slaScheduleDto.getDescription());
                dto.setType(calculationMethods.getSlaType());

                dto.setStartDateTime(slaExecution.getStartDateTime());
                dto.setEndDateTime(slaExecution.getEndDateTime());
                dto.setExecutionResult(slaExecution.getExecutionResult());
                dto.setOverallAchieved(slaExecution.getOverallAchieved());
                dto.setOverallMetersAffected(slaExecution.getOverallMetersAffected());
                dto.setP1Achieved(slaExecution.getP1Achieved());
                dto.setP1MetersAffected(slaExecution.getP1MetersAffected());
                dto.setP2Achieved(slaExecution.getP2Achieved());
                dto.setP2MetersAffected(slaExecution.getP2MetersAffected());
                dto.setP3Achieved(slaExecution.getP3Achieved());
                dto.setP3MetersAffected(slaExecution.getP3MetersAffected());
                dto.setZone(slaExecution.getZone());
                dto.setAudit(slaExecution.getAudit());

                return dto;
        }
}