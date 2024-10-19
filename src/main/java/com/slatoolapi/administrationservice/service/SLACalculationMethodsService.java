package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.*;
import com.slatoolapi.administrationservice.entity.*;
import com.slatoolapi.administrationservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLACalculationMethodsService {

    @Autowired
    private SLACalculationMethodsRepository slaCalculationMethodsRepository;

    @Autowired
    private SLACalculationModuleRepository slaCalculationModuleRepository;

    @Autowired
    private SLACalculationMethodsObjectiveRepository slaCalculationMethodsObjectiveRepository;

    @Autowired
    private SLAScheduleRepository slaScheduleRepository;

    @Autowired
    private SLAExecutionRepository slaExecutionRepository;

    @Autowired
    private SLACalculationModuleService slaCalculationModuleService;

    @Autowired
    private SLACalculationMethodsObjectiveService slaCalculationMethodsObjectiveService;

    @Autowired
    private SLADataMeterRepository slaDataMeterRepository;

    public SLACalculationMethodsDto save(SLACalculationMethodsPostDto slaCalculationMethodsPostDto) {

        SLACalculationModule slaCalculationModule = slaCalculationModuleRepository
                .findById(slaCalculationMethodsPostDto.getSlaCalculationMethods().getCalculationModuleId())
                .orElse(null);
        assert slaCalculationModule != null;

        if (slaCalculationMethodsPostDto.getSlaCalculationMethodsObjectives() == null) {
            return null;
        } else {
            SLACalculationMethods SLACalculationMethodsBuild = SLACalculationMethods.builder()
                    .name(slaCalculationMethodsPostDto.getSlaCalculationMethods().getName())
                    .description(slaCalculationMethodsPostDto.getSlaCalculationMethods().getDescription())
                    .calculationModuleId(
                            slaCalculationMethodsPostDto.getSlaCalculationMethods().getCalculationModuleId())
                    .slaType(slaCalculationMethodsPostDto.getSlaCalculationMethods().getSlaType()).plannedExecutionTime(
                            slaCalculationMethodsPostDto.getSlaCalculationMethods().getPlannedExecutionTime())
                    .build();

            SLACalculationMethods slaCalculationMethodsNew = slaCalculationMethodsRepository
                    .save(SLACalculationMethodsBuild);

            if (slaCalculationMethodsNew.getCalculationModuleId() == 29) {
                LocalDate date = LocalDate.now();
                Period period = Period.ofDays(-1);
                LocalDate substringDay = date.plus(period);

                for (SLACalculationMethodsObjective slaCalculationMethodsObjective : slaCalculationMethodsPostDto
                        .getSlaCalculationMethodsObjectives()) {
                    SLACalculationMethodsObjective slaCalculationMethodsObjectiveBuild = SLACalculationMethodsObjective
                            .builder().calculationMethodsId(slaCalculationMethodsNew.getId())
                            .type(slaCalculationMethodsObjective.getType())
                            .target(slaCalculationMethodsObjective.getTarget())
                            .targetUOM(slaCalculationMethodsObjective.getTargetUOM())
                            .compare(slaCalculationMethodsObjective.getCompare())
                            .limitTimes(slaCalculationMethodsObjective.getLimitTimes())
                            .numberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes())
                            .evaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes())
                            .evaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM())
                            .consecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes())
                            .consecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM()).build();
                    slaCalculationMethodsObjectiveRepository.save(slaCalculationMethodsObjectiveBuild);
                }

                if (slaDataMeterRepository.getSpatialAvailabilityHesZone(
                        substringDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) != null) {
                    List<String> zoneList = slaDataMeterRepository.getSpatialAvailabilityHesZone(
                            substringDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    for (String zone : zoneList) {
                        String plannedExecutionTime = slaCalculationMethodsNew.getPlannedExecutionTime();
                        String[] parts = plannedExecutionTime.split(":");
                        int executionTime = Integer.parseInt(parts[0]);
                        SLASchedule slaSchedule = SLASchedule.builder()
                                .calculationMethodsId(slaCalculationMethodsNew.getId())
                                .description(slaCalculationMethodsNew.getDescription()).active(true).frecuency("daily")
                                .executionTime(executionTime).build();
                        slaScheduleRepository.save(slaSchedule);

                        LocalDate now = LocalDate.now();

                        LocalDateTime dateTime = LocalDateTime.now();

                        String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String timeFormat = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                        String[] dateT = dateFormat.split("-");
                        String[] time = timeFormat.split(":");

                        int year = Integer.parseInt(dateT[0]);
                        int month = Integer.parseInt(dateT[1]);
                        int day = Integer.parseInt(dateT[2]);
                        int hour = Integer.parseInt(time[0]);
                        int minute = Integer.parseInt(time[1]);

                        LocalDateTime dtExtract = LocalDateTime.of(year, month, day, hour, minute);

                        SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                                .slaName(slaCalculationMethodsNew.getName()).status("pending").slaDate(now.plus(period))
                                .startDateTime(dtExtract).endDateTime(dtExtract).executionResult(""). // before "Not ok"
                                zone(zone).audit(false).build();

                        slaExecutionRepository.save(slaExecutionBuild);
                        return mapDto(slaCalculationMethodsNew);

                    }
                } else {
                    String plannedExecutionTime = slaCalculationMethodsNew.getPlannedExecutionTime();
                    String[] parts = plannedExecutionTime.split(":");
                    int executionTime = Integer.parseInt(parts[0]);

                    SLASchedule slaSchedule = SLASchedule.builder()
                            .calculationMethodsId(slaCalculationMethodsNew.getId())
                            .description(slaCalculationMethodsNew.getDescription()).active(true).frecuency("daily")
                            .executionTime(executionTime).build();
                    slaScheduleRepository.save(slaSchedule);
                    LocalDate now = LocalDate.now();

                    LocalDateTime dateTime = LocalDateTime.now();

                    String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String timeFormat = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                    String[] dateT = dateFormat.split("-");
                    String[] time = timeFormat.split(":");

                    int year = Integer.parseInt(dateT[0]);
                    int month = Integer.parseInt(dateT[1]);
                    int day = Integer.parseInt(dateT[2]);
                    int hour = Integer.parseInt(time[0]);
                    int minute = Integer.parseInt(time[1]);

                    LocalDateTime dtExtract = LocalDateTime.of(year, month, day, hour, minute);

                    SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                            .slaName(slaCalculationMethodsNew.getName()).status("error").slaDate(now.plus(period))
                            .startDateTime(dtExtract).endDateTime(dtExtract).executionResult("Not ok"). // before "Not
                                                                                                        // ok"
                            zone("").audit(false).build();
                    slaExecutionRepository.save(slaExecutionBuild);
                    return mapDto(slaCalculationMethodsNew);
                }
            }

            for (SLACalculationMethodsObjective slaCalculationMethodsObjective : slaCalculationMethodsPostDto
                    .getSlaCalculationMethodsObjectives()) {
                SLACalculationMethodsObjective slaCalculationMethodsObjectiveBuild = SLACalculationMethodsObjective
                        .builder().calculationMethodsId(slaCalculationMethodsNew.getId())
                        .type(slaCalculationMethodsObjective.getType())
                        .target(slaCalculationMethodsObjective.getTarget())
                        .targetUOM(slaCalculationMethodsObjective.getTargetUOM())
                        .compare(slaCalculationMethodsObjective.getCompare())
                        .limitTimes(slaCalculationMethodsObjective.getLimitTimes())
                        .numberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes())
                        .evaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes())
                        .evaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM())
                        .consecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes())
                        .consecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM()).build();
                slaCalculationMethodsObjectiveRepository.save(slaCalculationMethodsObjectiveBuild);
            }
            String plannedExecutionTime = slaCalculationMethodsNew.getPlannedExecutionTime();
            String[] parts = plannedExecutionTime.split(":");
            int executionTime = Integer.parseInt(parts[0]);
            SLASchedule slaSchedule = SLASchedule.builder().calculationMethodsId(slaCalculationMethodsNew.getId())
                    .description(slaCalculationMethodsNew.getDescription()).active(true).frecuency("daily")
                    .executionTime(executionTime).build();
            slaScheduleRepository.save(slaSchedule);
            LocalDate now = LocalDate.now();
            Period period = Period.ofDays(-1);
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

            LocalDateTime dtExtract = LocalDateTime.of(year, month, day, hour, minute);

            SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
                    .slaName(slaCalculationMethodsNew.getName()).status("pending").slaDate(now.plus(period))
                    .startDateTime(dtExtract).endDateTime(dtExtract).executionResult("").audit(false).build();

            slaExecutionRepository.save(slaExecutionBuild);
            return mapDto(slaCalculationMethodsNew);
        }
    }

    public List<SLACalculationMethodsDto> getAll() {
        List<SLACalculationMethods> slaCalculationMethods = slaCalculationMethodsRepository.findAll();

        return slaCalculationMethods.stream().map(this::mapDto).collect(Collectors.toList());
    }

    public SLACalculationMethodsDto getById(long id) {
        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository.findById(id).orElse(null);
        if (slaCalculationMethods != null) {
            return mapDto(slaCalculationMethods);
        }
        return null;
    }

    public SLACalculationMethodsDto update(long id, SLACalculationMethodsPostDto slaCalculationMethodsPostDto) {

        SLACalculationMethods slaCalculationMethodsUpdated = slaCalculationMethodsRepository.findById(id).orElse(null);
        assert slaCalculationMethodsUpdated != null;
        if (slaCalculationMethodsPostDto == null) {
            return null;
        }

        if (slaCalculationMethodsPostDto.getSlaCalculationMethods().getCalculationModuleId() != 0) {
            SLACalculationModule slaCalculationModule = slaCalculationModuleRepository
                    .findById(slaCalculationMethodsPostDto.getSlaCalculationMethods().getCalculationModuleId())
                    .orElse(null);
            assert slaCalculationModule != null;
            slaCalculationMethodsUpdated.setCalculationModuleId(slaCalculationModule.getId());
        }

        SLASchedule slaSchedule = slaScheduleRepository.findByCalculationMethodsId(id).orElse(null);
        if (slaCalculationMethodsPostDto.getSlaCalculationMethods().getName() != null) {
            slaCalculationMethodsUpdated.setName(slaCalculationMethodsPostDto.getSlaCalculationMethods().getName());
        }

        if (slaCalculationMethodsPostDto.getSlaCalculationMethods().getDescription() != null) {
            slaCalculationMethodsUpdated
                    .setDescription(slaCalculationMethodsPostDto.getSlaCalculationMethods().getDescription());
            if (slaSchedule != null) {
                slaSchedule.setDescription(slaCalculationMethodsPostDto.getSlaCalculationMethods().getDescription());
                slaScheduleRepository.save(slaSchedule);
            }
        }

        if (slaCalculationMethodsPostDto.getSlaCalculationMethods().getSlaType() != null) {
            slaCalculationMethodsUpdated
                    .setSlaType(slaCalculationMethodsPostDto.getSlaCalculationMethods().getSlaType());
        }

        if (slaCalculationMethodsPostDto.getSlaCalculationMethods().getPlannedExecutionTime() != null) {
            slaCalculationMethodsUpdated.setPlannedExecutionTime(
                    slaCalculationMethodsPostDto.getSlaCalculationMethods().getPlannedExecutionTime());
            if (slaSchedule != null) {
                String[] parts = slaCalculationMethodsPostDto.getSlaCalculationMethods().getPlannedExecutionTime()
                        .split(":");
                int executionTime = Integer.parseInt(parts[0]);
                slaSchedule.setExecutionTime(executionTime);
                slaScheduleRepository.save(slaSchedule);
            }
        }

        List<SLACalculationMethodsObjective> slaCalculationMethodsObjectiveList = slaCalculationMethodsObjectiveRepository
                .findByCalculationMethodsId(id);
        if (slaCalculationMethodsObjectiveList != null) {
            ArrayList<SLACalculationMethodsObjective> slaCalculationMethodsObjectiveArrayList = new ArrayList<>();
            for (int x = 0; x < slaCalculationMethodsPostDto.getSlaCalculationMethodsObjectives().size(); x++) {
                slaCalculationMethodsObjectiveArrayList
                        .add(slaCalculationMethodsPostDto.getSlaCalculationMethodsObjectives().get(x));
            }
            for (SLACalculationMethodsObjective slaCalculationMethodsObjective : slaCalculationMethodsObjectiveArrayList) {
                slaCalculationMethodsObjective.setCalculationMethodsId(id);
                slaCalculationMethodsObjective.setType(slaCalculationMethodsObjective.getType());
                slaCalculationMethodsObjective.setTarget(slaCalculationMethodsObjective.getTarget());
                slaCalculationMethodsObjective.setTargetUOM(slaCalculationMethodsObjective.getTargetUOM());
                slaCalculationMethodsObjective.setCompare(slaCalculationMethodsObjective.getCompare());
                slaCalculationMethodsObjective.setLimitTimes(slaCalculationMethodsObjective.getLimitTimes());
                slaCalculationMethodsObjective.setNumberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes());
                slaCalculationMethodsObjective.setEvaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes());
                slaCalculationMethodsObjective
                        .setEvaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM());
                slaCalculationMethodsObjective
                        .setConsecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes());
                slaCalculationMethodsObjective
                        .setConsecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM());
                slaCalculationMethodsObjectiveRepository.save(slaCalculationMethodsObjective);
            }
        }

        slaCalculationMethodsRepository.save(slaCalculationMethodsUpdated);
        return mapDto(slaCalculationMethodsUpdated);

    }

    public boolean delete(long id) {
        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository.findById(id).orElse(null);
        assert slaCalculationMethods != null;

        List<SLASchedule> slaScheduleList = slaScheduleRepository.findAll();

        for (SLASchedule slaSchedule : slaScheduleList) {
            if (slaSchedule.getCalculationMethodsId() == slaCalculationMethods.getId()) {
                List<SLAExecution> slaExecutions = slaExecutionRepository.findByScheduleId(slaSchedule.getId());
                for (SLAExecution slaExecution : slaExecutions) {
                    slaExecutionRepository.deleteById(slaExecution.getId());
                }
                slaScheduleRepository.deleteById(slaSchedule.getId());
            }
        }
        List<SLACalculationMethodsObjective> slaCalculationMethodsObjectives = slaCalculationMethodsObjectiveRepository
                .findByCalculationMethodsId(slaCalculationMethods.getId());
        for (SLACalculationMethodsObjective slaCalculationMethodsObjective : slaCalculationMethodsObjectives) {
            slaCalculationMethodsObjectiveRepository.deleteById(slaCalculationMethodsObjective.getId());
        }
        slaCalculationMethodsRepository.deleteById(id);
        return true;

    }

    public SLACalculationMethodsDto mapDto(SLACalculationMethods slaCalculationMethods) {

        SLACalculationModule slaCalculationModule = slaCalculationModuleRepository
                .findById(slaCalculationMethods.getCalculationModuleId()).orElse(null);
        if (slaCalculationModule == null) {
            return null;
        }
        SLACalculationModuleDto slaCalculationModuleDto = slaCalculationModuleService.mapDto(slaCalculationModule);

        List<SLACalculationMethodsObjective> slaCalculationMethodsObjectives = slaCalculationMethodsObjectiveRepository
                .findByCalculationMethodsId(slaCalculationMethods.getId());
        List<SLACalculationMethodObjectiveDto> slaCalculationMethodObjectiveDtos = new ArrayList<>();
        for (SLACalculationMethodsObjective slaCalculationMethodsObjective : slaCalculationMethodsObjectives) {
            SLACalculationMethodObjectiveDto slaCalculationMethodObjectiveDto = slaCalculationMethodsObjectiveService
                    .mapDto(slaCalculationMethodsObjective);
            slaCalculationMethodObjectiveDtos.add(slaCalculationMethodObjectiveDto);
        }

        SLACalculationMethodsDto dto = new SLACalculationMethodsDto();
        dto.setId(slaCalculationMethods.getId());
        dto.setName(slaCalculationMethods.getName());
        dto.setDescription(slaCalculationMethods.getDescription());
        dto.setCalculationModuleId(slaCalculationModuleDto);
        dto.setSlaType(slaCalculationMethods.getSlaType());
        dto.setPlannedExecutionTime(slaCalculationMethods.getPlannedExecutionTime());
        dto.setSlaCalculationMethodObjectiveDtos(slaCalculationMethodObjectiveDtos);

        return dto;
    }
}
