package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.SLADailyConsolidationDto;
import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import com.slatoolapi.administrationservice.entity.SLADailyConsolidation;
import com.slatoolapi.administrationservice.entity.SLAExecution;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsObjectiveRepository;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsRepository;
import com.slatoolapi.administrationservice.repository.SLADailyConsolidationRepository;
import com.slatoolapi.administrationservice.repository.SLAExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@EnableScheduling
@Component
@Slf4j
public class SLADailyConsolidationService {

    Boolean demandMode = false;

    @Value("${spring.application.timezone}")
    private static final String TIME_ZONE = "UTC";

    @Autowired
    SLACalculationMethodsRepository slaCalculationMethodsRepository;

    @Autowired
    SLACalculationMethodsObjectiveRepository slaCalculationMethodsObjectiveRepository;

    @Autowired
    SLAExecutionRepository slaExecutionRepository;

    @Autowired
    SLADailyConsolidationRepository slaDailyConsolidationRepository;

    public List<SLADailyConsolidation> autoCallMonthly() {
        return slaDailyConsolidationRepository.findAll();
    }

    public boolean monthlySucess(float Average, float Target, String c) {
        if (c != null) {
            if ("<".equals(c)) {
                if (Average < Target) {
                    return true;
                }
            } else if ("<=".equals(c)) {
                if (Average <= Target) {
                    return true;
                }
            } else if (">".equals(c)) {
                if (Average > Target) {
                    return true;
                }
            } else if (">=".equals(c)) {
                if (Average >= Target) {
                    return true;
                }
            } else if ("=".equals(c)) {
                if (Average == Target) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Scheduled(cron = "30 59 23 * * ?", zone = TIME_ZONE)
    public void resultMonthly() {
        resultMonthly(Optional.empty(), Optional.empty());

    }

    public void resultMonthly(Optional<Integer> pYear, Optional<Integer> pMonth) {

        LocalDate date;
        Period period;
        YearMonth month;
        LocalDate startMonthly;
        LocalDate endMonthly;
        Integer yearGet;
        String year;
        LocalDate lastDate;
        Integer ac = 0;
        Integer cc = 0;
        Integer ndPeriod;

        if (pYear.isPresent() && pMonth.isPresent()) {
            slaDailyConsolidationRepository.deleteSlaDateConsolidation(pYear.get(), pMonth.get());
            demandMode = true;
            ndPeriod = 0;

            month = YearMonth.of(pYear.get(), pMonth.get());
            period = Period.ofDays(0);
            startMonthly = month.atDay(1);
            endMonthly = month.atEndOfMonth();

            date = endMonthly;
            lastDate = endMonthly;

            yearGet = date.getYear();
            year = yearGet.toString();

        } else {
            demandMode = false;
            ndPeriod = -1;

            date = LocalDate.now();
            lastDate = LocalDate.now();
            period = Period.ofDays(-2);
            month = YearMonth.from(date.plus(period));
            startMonthly = month.atDay(1);
            endMonthly = month.atEndOfMonth();
            yearGet = date.getYear();
            year = yearGet.toString();

        }

        List<SLADailyConsolidation> slaDailyConsolidationList = new ArrayList<>();
        SLADailyConsolidation slaDailyConsolidation = SLADailyConsolidation.builder().build();
        SLADailyConsolidationDto slaDailyConsolidationDto = new SLADailyConsolidationDto();
        List<SLACalculationMethodsObjective> slaCalculationMethodsObjectiveList = new ArrayList<>();
        List<SLACalculationMethods> slaExist = slaCalculationMethodsRepository.getExtractExisting();

        for (SLACalculationMethods slaCalculationMethods : slaExist) {
            float P1average;
            float P2average;
            float P3average;
            float OverallAverage;
            Float OvAffects;
            String name = "";
            String getZone = "";
            String getZoneParam = null;
            String type_uom_p1 = "";
            String type_uom_p2 = "";
            String type_uom_p3 = "";
            String type_uom_overall = "";

            float P1 = 0f;
            float P1Affects = 0f;
            float P2 = 0f;
            float P2Affects = 0f;
            float P3 = 0f;
            float P3Affects = 0f;
            float Overall = 0f;
            float OverallAffects = 0f;

            boolean p1_success = true;
            boolean p2_success = true;
            boolean p3_success = true;
            boolean overall_success = true;

            String sla = slaCalculationMethods.getName();
            String desc = slaCalculationMethods.getDescription();
            long slaId = slaCalculationMethods.getId();
            String typeFilter = slaCalculationMethods.getSlaType();
            long id = slaCalculationMethods.getId();
            long cmId = slaCalculationMethods.getCalculationModuleId();

            Integer nZone;
            List<String> listZone;
            Boolean bandZone;
            if (id == 23) {
                bandZone = true;

                listZone = slaExecutionRepository.getResultExecutionZone(sla,
                        startMonthly.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        endMonthly.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                if (listZone != null && !listZone.isEmpty()) {
                    nZone = listZone.size();
                } else {

                    listZone = new ArrayList<>();
                    listZone.add(null);
                    nZone = listZone.size();
                }

            } else {
                bandZone = false;
                listZone = new ArrayList<>();
                listZone.add(null);
                nZone = listZone.size();

            }

            for (String zone : listZone) {

                List<SLAExecution> slaExecutionList = new ArrayList<>();
                List<SLAExecution> slaExecutionListIntern = slaExecutionRepository.getResultExecution(sla,
                        startMonthly.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        endMonthly.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), zone);

                float maxP1Affects = 0;
                float maxP2Affects = 0;
                float maxP3Affects = 0;
                float maxOverallAffects = 0;

                for (SLAExecution slaExecution : slaExecutionListIntern) {
                    LocalDate slaNewDate = slaExecution.getSlaDate();

                    slaExecutionList.add(slaExecution);

                    if (slaExecution != null && "done".equals(slaExecution.getStatus()) && (!slaExecution.getAudit())) {

                        getZone = slaExecution.getZone();
                        name = sla;
                        lastDate = slaExecution.getSlaDate();

                        float getP1 = slaExecution.getP1Achieved();
                        float getP1Affects = slaExecution.getP1MetersAffected();
                        float getP2 = slaExecution.getP2Achieved();
                        float getP2Affects = slaExecution.getP2MetersAffected();
                        float getP3 = slaExecution.getP3Achieved();
                        float getP3Affects = slaExecution.getP3MetersAffected();
                        float getOverall = slaExecution.getOverallAchieved();
                        float getOverallAffects = slaExecution.getOverallMetersAffected();

                        maxP1Affects = (getP1Affects > maxP1Affects) ? getP1Affects : maxP1Affects;
                        maxP2Affects = (getP2Affects > maxP2Affects) ? getP2Affects : maxP2Affects;
                        maxP3Affects = (getP3Affects > maxP3Affects) ? getP3Affects : maxP3Affects;
                        maxOverallAffects = (getOverallAffects > maxOverallAffects) ? getOverallAffects
                                : maxOverallAffects;

                        P1 += getP1 * getP1Affects;
                        P1Affects += getP1Affects;
                        P2 += getP2 * getP2Affects;
                        P2Affects += getP2Affects;
                        P3 += getP3 * getP3Affects;
                        P3Affects += getP3Affects;
                        Overall += getOverall * getOverallAffects;
                        OverallAffects += getOverallAffects;
                    }
                }
                P1average = (P1Affects == 0) ? 0f : Float.valueOf(String.format(Locale.US, "%.2f", (P1 / P1Affects)));
                P2average = (P2Affects == 0) ? 0f : Float.valueOf(String.format(Locale.US, "%.2f", (P2 / P2Affects)));
                P3average = (P3Affects == 0) ? 0f : Float.valueOf(String.format(Locale.US, "%.2f", (P3 / P3Affects)));
                OverallAverage = (OverallAffects == 0) ? 0f
                        : Float.valueOf(String.format(Locale.US, "%.2f", (Overall / OverallAffects)));
                OvAffects = OverallAffects;

                slaCalculationMethodsObjectiveList = slaCalculationMethodsObjectiveRepository
                        .findByCalculationMethodsId(slaId);
                int x = 0;

                for (SLACalculationMethodsObjective methodsObjective : slaCalculationMethodsObjectiveList) {

                    type_uom_p1 = "p1".equals(methodsObjective.getType()) ? methodsObjective.getTargetUOM()
                            : type_uom_p1;
                    type_uom_p2 = "p2".equals(methodsObjective.getType()) ? methodsObjective.getTargetUOM()
                            : type_uom_p2;
                    type_uom_p3 = "p3".equals(methodsObjective.getType()) ? methodsObjective.getTargetUOM()
                            : type_uom_p3;
                    type_uom_overall = "Overall".equals(methodsObjective.getType()) ? methodsObjective.getTargetUOM()
                            : type_uom_overall;

                    P1average = (P1Affects == 0) ? (("%".equals(type_uom_p1)) ? 100 : 0) : P1average;
                    P2average = (P2Affects == 0) ? (("%".equals(type_uom_p2)) ? 100 : 0) : P2average;
                    P3average = (P3Affects == 0) ? (("%".equals(type_uom_p3)) ? 100 : 0) : P3average;
                    OverallAverage = (OverallAffects == 0) ? (("%".equals(type_uom_overall)) ? 100 : 0)
                            : OverallAverage;
                    if ("HES".equals(typeFilter)) {
                        float Target = methodsObjective.getTarget();
                        String compare = methodsObjective.getCompare();
                        switch (methodsObjective.getType()) {
                            case "p1":
                                p1_success = monthlySucess(P1average, Target, compare);
                                break;
                            case "p2":
                                p2_success = monthlySucess(P2average, Target, compare);
                                break;
                            case "p3":
                                p3_success = monthlySucess(P3average, Target, compare);
                                break;
                            case "Overall":
                                overall_success = monthlySucess(OverallAverage, Target, compare);
                                break;
                        }

                    } else {
                        if (lastDate.plus(Period.ofDays(ndPeriod)).isEqual(endMonthly)) {
                            float Target = methodsObjective.getTarget();
                            String compare = methodsObjective.getCompare();
                            switch (methodsObjective.getType()) {
                                case "p1":
                                    p1_success = monthlySucess(P1average, Target, compare);
                                    break;
                                case "p2":
                                    p2_success = monthlySucess(P2average, Target, compare);
                                    break;
                                case "p3":
                                    p3_success = monthlySucess(P3average, Target, compare);
                                    break;
                                case "Overall":
                                    overall_success = monthlySucess(OverallAverage, Target, compare);
                                    break;
                            }

                        } else {
                            switch (methodsObjective.getType()) {
                                case "p1":
                                    if (methodsObjective.getNumberOfTimes() > 0) {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            Integer numberofTimes = (int) methodsObjective.getNumberOfTimes();
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();

                                            ArrayList<String> timesNotSucceededP1 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP1(name, yearGet, numberofTimes);

                                            p1_success = !(timesNotSucceededP1 != null
                                                    && timesNotSucceededP1.size() > (int) consecutiveTimes);

                                        } else {

                                            int getNumberOfTimesP1 = slaDailyConsolidationRepository.getNumberOfTimesP1(
                                                    name,
                                                    month.getYear(), month.getMonthValue());

                                            p1_success = !(getNumberOfTimesP1 > (int) methodsObjective
                                                    .getNumberOfTimes());

                                        }
                                    } else {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {

                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededP1 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP1(name, yearGet, 0);

                                            p1_success = !(timesNotSucceededP1 != null
                                                    && timesNotSucceededP1.size() > (int) consecutiveTimes);

                                        } else {
                                            float Target = methodsObjective.getTarget();
                                            String compare = methodsObjective.getCompare();
                                            p1_success = monthlySucess(P1average, Target, compare);
                                        }
                                    }
                                    break;
                                case "p2":
                                    if (methodsObjective.getNumberOfTimes() > 0) {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            Integer numberofTimes = (int) methodsObjective.getNumberOfTimes();
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededP2 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP2(name, yearGet, numberofTimes);
                                            p2_success = !(timesNotSucceededP2 != null
                                                    && timesNotSucceededP2.size() > (int) consecutiveTimes);

                                        } else {

                                            int getNumberOfTimesP2 = slaDailyConsolidationRepository.getNumberOfTimesP2(
                                                    name,
                                                    month.getYear(), month.getMonthValue());

                                            p2_success = !(getNumberOfTimesP2 > (int) methodsObjective
                                                    .getNumberOfTimes());

                                        }
                                    } else {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededP2 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP2(name, yearGet, 0);

                                            p2_success = !(timesNotSucceededP2 != null
                                                    && timesNotSucceededP2.size() > (int) consecutiveTimes);

                                        } else {
                                            float Target = methodsObjective.getTarget();
                                            String compare = methodsObjective.getCompare();
                                            p2_success = monthlySucess(P2average, Target, compare);
                                        }
                                    }
                                    break;
                                case "p3":
                                    if (methodsObjective.getNumberOfTimes() > 0) {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            Integer numberofTimes = (int) methodsObjective.getNumberOfTimes();
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededP3 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP3(name, yearGet, numberofTimes);

                                            p3_success = !(timesNotSucceededP3 != null
                                                    && timesNotSucceededP3.size() > (int) consecutiveTimes);

                                        } else {

                                            int getNumberOfTimesP3 = slaDailyConsolidationRepository.getNumberOfTimesP3(
                                                    name,
                                                    month.getYear(), month.getMonthValue());

                                            p3_success = !(getNumberOfTimesP3 > (int) methodsObjective
                                                    .getNumberOfTimes());

                                        }
                                    } else {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededP3 = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededP3(name, yearGet, 0);

                                            p3_success = !(timesNotSucceededP3 != null
                                                    && timesNotSucceededP3.size() > (int) consecutiveTimes);

                                        } else {
                                            float Target = methodsObjective.getTarget();
                                            String compare = methodsObjective.getCompare();
                                            p3_success = monthlySucess(P3average, Target, compare);
                                        }
                                    }
                                    break;
                                case "Overall":
                                    if (methodsObjective.getNumberOfTimes() > 0) {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            Integer numberofTimes = (int) methodsObjective.getNumberOfTimes();
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededOverall = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededOverall(name, yearGet, numberofTimes);

                                            overall_success = !(timesNotSucceededOverall != null
                                                    && timesNotSucceededOverall.size() > (int) consecutiveTimes);

                                        } else {

                                            int getNumberOfTimesOverall = slaDailyConsolidationRepository
                                                    .getNumberOfTimesOverall(name, month.getYear(),
                                                            month.getMonthValue());

                                            overall_success = !(getNumberOfTimesOverall > (int) methodsObjective
                                                    .getNumberOfTimes());

                                        }
                                    } else {
                                        if (methodsObjective.getConsecutiveTimes() > 0) {
                                            float consecutiveTimes = methodsObjective.getConsecutiveTimes();
                                            ArrayList<String> timesNotSucceededOverall = slaDailyConsolidationRepository
                                                    .getNumberOfTimesNotsucceededOverall(name, yearGet, 0);

                                            overall_success = !(timesNotSucceededOverall != null
                                                    && timesNotSucceededOverall.size() > (int) consecutiveTimes);

                                        } else {
                                            float Target = methodsObjective.getTarget();
                                            String compare = methodsObjective.getCompare();
                                            overall_success = monthlySucess(OverallAverage, Target, compare);
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }

                if (bandZone) {
                    getZoneParam = getZone;
                } else {
                    getZoneParam = null;
                }

                if (!name.isEmpty() && slaDailyConsolidationRepository
                        .getDetails(
                                lastDate.plus(Period.ofDays(ndPeriod))
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                name, getZoneParam)
                        .equals(0)) {

                    slaDailyConsolidation = SLADailyConsolidation.builder().sla_name(name).description(desc)
                            .sla_date(lastDate.plus(Period.ofDays(ndPeriod)))
                            .p1_achieved(P1average).p2_achieved(P2average).p3_achieved(P3average)
                            .total_meters_p1(maxP1Affects).total_meters_p2(maxP2Affects).total_meters_p3(maxP3Affects)
                            .overall_achieved(OverallAverage).total_meters_overall(maxOverallAffects).zone(getZone)
                            .p1_success(p1_success).p2_success(p2_success).p3_success(p3_success)
                            .overall_success(overall_success).type(typeFilter).type_uom_p1(type_uom_p1)
                            .type_uom_p2(type_uom_p2).type_uom_p3(type_uom_p3).type_uom_overall(type_uom_overall)
                            .sla_id(slaId).build();

                    if (slaDailyConsolidation != null) {
                        cc++;
                        slaDailyConsolidationList.add(slaDailyConsolidation);
                        slaDailyConsolidationRepository.save(slaDailyConsolidation);
                    } else {
                        log.info("slaDailyConsolidation Null");
                    }
                } else {

                    if (!name.isEmpty() && demandMode) {

                        SLADailyConsolidation existingRecord = slaDailyConsolidationRepository
                                .findBySlaDateAndSlaNameAndZone(
                                        lastDate.plus(Period.ofDays(ndPeriod))
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        name, getZoneParam);

                        existingRecord.setSla_name(name);
                        existingRecord.setDescription(desc);
                        existingRecord.setSla_date(lastDate.plus(Period.ofDays(ndPeriod)));
                        existingRecord.setP1_achieved(P1average);
                        existingRecord.setP2_achieved(P2average);
                        existingRecord.setP3_achieved(P3average);
                        existingRecord.setTotal_meters_p1(maxP1Affects);
                        existingRecord.setTotal_meters_p2(maxP2Affects);
                        existingRecord.setTotal_meters_p3(maxP3Affects);
                        existingRecord.setOverall_achieved(OverallAverage);
                        existingRecord.setTotal_meters_overall(maxOverallAffects);
                        existingRecord.setZone(getZone);
                        existingRecord.setP1_success(p1_success);
                        existingRecord.setP2_success(p2_success);
                        existingRecord.setP3_success(p3_success);
                        existingRecord.setOverall_success(overall_success);
                        existingRecord.setType(typeFilter);
                        existingRecord.setType_uom_p1(type_uom_p1);
                        existingRecord.setType_uom_p2(type_uom_p2);
                        existingRecord.setType_uom_p3(type_uom_p3);
                        existingRecord.setType_uom_overall(type_uom_overall);

                        ac++;
                        slaDailyConsolidationList.add(existingRecord);
                        slaDailyConsolidationRepository.save(existingRecord);

                    } else {

                    }
                }
            }

        }
    }
}
