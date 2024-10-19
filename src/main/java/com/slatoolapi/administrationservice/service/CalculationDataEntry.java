package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.repository.SLADataEntryRepository;
import com.slatoolapi.administrationservice.repository.SLADataMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CalculationDataEntry {
    @Autowired
    private SLADataMeterRepository slaDataMeterRepository;

    @Autowired
    private SLADataEntryRepository slaDataEntryRepository;

    Map<Boolean, Float> NotConnectingMetersP1(String typeId, String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {

            float activeMettersDto;
            float totalMonthlyAssigned;
            String[] parts;
            List<String> notConnectingMeters;

            if (slaDataEntryRepository.getBusinessDataEntryVerified(dmy) > 3) {
                ret.put(false, 0.0f);
                return ret;
            }

            if ((slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", "P1",
                    dmy)) != null) {
                activeMettersDto = slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED",
                        "P1", dmy);
            } else {
                activeMettersDto = 0;
            }

            if (slaDataEntryRepository.businessDataEntry(dmy, typeId) != null) {
                notConnectingMeters = slaDataEntryRepository.businessDataEntry(dmy, typeId);
                if (notConnectingMeters.isEmpty()) {
                    ret.put(false, 0.0f);
                    return ret;
                }
                String str = notConnectingMeters.get(0);
                String data = str;
                parts = data.split(",");
                String dateEnd = parts[0];
                String dateStart = parts[1];
                String dataTypeId = parts[2];
                Integer dataId = Integer.parseInt(dataTypeId);
                String value = parts[3];
                float valueFloat = Float.parseFloat(value);
                if (dataId == 1) {
                    if (valueFloat == 0) {
                        ret.put(false, 0.0f);
                        return ret;
                    }
                    totalMonthlyAssigned = (activeMettersDto / valueFloat) * 100;
                    ret.put(true, totalMonthlyAssigned);
                    return ret;
                }
            }
            ret.put(false, 0.0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0.0f);
            return ret;
        }
    }

    Map<Boolean, Float> NotConnectingMetersP2(String typeId, String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {

            float activeMettersDto;
            float totalMonthlyAssigned;
            String[] parts;
            List<String> notConnectingMeters;

            if (slaDataEntryRepository.getBusinessDataEntryVerified(dmy) > 3) {
                ret.put(false, 0.0f);
                return ret;
            }

            if ((slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", "P2",
                    dmy)) != null) {
                activeMettersDto = (float) slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE",
                        "DISCONNECTED", "P2", dmy);
            } else {
                activeMettersDto = 0;
            }

            if ((slaDataEntryRepository.businessDataEntry(dmy, typeId)) != null) {
                notConnectingMeters = slaDataEntryRepository.businessDataEntry(dmy, typeId);
                if (notConnectingMeters.isEmpty()) {
                    ret.put(false, 0.0f);
                    return ret;
                }
                String str = notConnectingMeters.get(0);
                String data = str;
                parts = data.split(",");
                String dateEnd = parts[0];
                String dateStart = parts[1];
                String dataTypeId = parts[2];
                Integer dataId = Integer.parseInt(dataTypeId);
                String value = parts[3];
                float valueFloat = Float.parseFloat(value);
                if (dataId == 2) {
                    if (valueFloat == 0) {
                        ret.put(false, 0.0f);
                        return ret;
                    }
                    totalMonthlyAssigned = (activeMettersDto / valueFloat) * 100;
                    Formatter formatter = new Formatter();
                    formatter.format("%.2f", totalMonthlyAssigned);
                    String FT = formatter.toString();
                    float result = Float.parseFloat(FT);
                    ret.put(true, result);
                    return ret;
                } else {
                    ret.put(false, 0.0f);
                    return ret;
                }
            }
            ret.put(false, 0.0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0.0f);
            return ret;
        }
    }

    Map<Boolean, Float> NotConnectingMetersP3(String typeId, String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {

            float activeMettersDto;
            float totalMonthlyAssigned;
            String[] parts;
            List<String> notConnectingMeters;

            if (slaDataEntryRepository.getBusinessDataEntryVerified(dmy) > 3) {
                ret.put(false, 0.0f);
                return ret;
            }

            if ((slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", "P3",
                    dmy)) != null) {
                activeMettersDto = (float) slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE",
                        "DISCONNECTED", "P3", dmy);
            } else {
                activeMettersDto = 0;
            }

            if ((slaDataEntryRepository.businessDataEntry(dmy, typeId)) != null) {
                notConnectingMeters = slaDataEntryRepository.businessDataEntry(dmy, typeId);
                if (notConnectingMeters.isEmpty()) {
                    ret.put(false, 0.0f);
                    return ret;
                }
                String str = notConnectingMeters.get(0);
                String data = str;
                parts = data.split(",");
                String dateEnd = parts[0];
                String dateStart = parts[1];
                String dataTypeId = parts[2];
                Integer dataId = Integer.parseInt(dataTypeId);
                String value = parts[3];
                float valueFloat = Float.parseFloat(value);
                if (dataId == 3) {
                    if (valueFloat == 0) {
                        ret.put(false, 0.0f);
                        return ret;
                    }
                    totalMonthlyAssigned = (activeMettersDto / valueFloat) * 100;
                    Formatter formatter = new Formatter();
                    formatter.format("%.2f", totalMonthlyAssigned);
                    String FT = formatter.toString();
                    float result = Float.parseFloat(FT);
                    ret.put(true, result);
                    return ret;
                } else {
                    ret.put(false, 0.0f);
                    return ret;
                }
            }
            ret.put(false, 0.0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0.0f);
            return ret;
        }
    }

    public float NotConnectingMetersAffects(String Px, String dmy) {
        try {

            float activeMettersDto;

            if (slaDataEntryRepository.getBusinessDataEntryVerified(dmy) > 3) {
                return 0;
            }

            if ((slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", Px,
                    dmy)) != null) {
                activeMettersDto = (float) slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE",
                        "DISCONNECTED", Px, dmy);
                Formatter formatter = new Formatter();
                formatter.format("%.2f", activeMettersDto);
                String FT = formatter.toString();
                float result = Float.parseFloat(FT);
                return result;
            } else {
                activeMettersDto = 0;
                return activeMettersDto;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    Map<Boolean, Float> ExceedingTheMaximumPlannedOutageOccasionsOrDuration(String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {
            // year
            Date year = new SimpleDateFormat("yyyy-MM-dd").parse(dmy);
            Calendar c = Calendar.getInstance();
            c.setTime(year);
            Date dateYYYY = c.getTime();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String yearOnly = yearFormat.format(dateYYYY);

            ArrayList<String> plannedOutage;
            float amass = 0;
            float x = 0;
            float timeLimit = 0;
            if (slaDataEntryRepository.getTimeLimitPlannedOutage() != null) {
                timeLimit = slaDataEntryRepository.getTimeLimitPlannedOutage();
            }
            ;

            if (slaDataEntryRepository.getExceedingTheMaximumPlannedOutageOccasionsOrDuration() != null) {
                plannedOutage = slaDataEntryRepository.getExceedingTheMaximumPlannedOutageOccasionsOrDuration();
                for (String s : plannedOutage) { // -> change for more efficient
                    String[] splitString = s.split(",");
                    if (splitString[0].contains(yearOnly)) {
                        ++x;
                        if (splitString[0].contains(dmy)) {
                            Float value = Float.valueOf(splitString[1]);
                            amass += value;
                        }
                    }
                }
            }

            if (x > 8) {
                ret.put(true, amass);
                return ret;
            } else if (x == 0) {
                ret.put(true, 0f);
                return ret;
            } else if (amass > timeLimit) {
                float overallAchieved = (amass - timeLimit);
                Formatter formatter = new Formatter();
                formatter.format("%.2f", overallAchieved);
                String FT = formatter.toString();
                float finalSL = Float.parseFloat(FT);
                ret.put(true, finalSL);
                return ret;
            }
        } catch (Exception e) {
            ret.put(false, 0f);
            e.printStackTrace();
            return ret;
        }
        ret.put(true, 0f);
        return ret;
    }

    public float ExceedingTheMaximumUnplannedOutageOccasionsOrDuration(String dmy) {
        try {

            Date year = new SimpleDateFormat("yyyy-MM-dd").parse(dmy);
            Calendar c = Calendar.getInstance();
            c.setTime(year);
            Date dateYYYY = c.getTime();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String yearOnly = yearFormat.format(dateYYYY);

            ArrayList<String> unPlannedOutage;
            float amass = 0;
            float x = 0;

            float timeLimit = 0;
            if (slaDataEntryRepository.getTimeLimitUnplannedOutage() != null) {
                timeLimit = slaDataEntryRepository.getTimeLimitUnplannedOutage();
            }
            ;

            if (slaDataEntryRepository.getExceedingTheMaximumUnplannedOutageOccasionsOrDuration() != null) {
                unPlannedOutage = slaDataEntryRepository.getExceedingTheMaximumUnplannedOutageOccasionsOrDuration();
                for (Integer i = 0; i < unPlannedOutage.size(); i++) {
                    String[] splitString = unPlannedOutage.get(i).split(",");
                    if (splitString[0].contains(yearOnly)) {
                        ++x;
                        if (splitString[0].contains(dmy)) {
                            Float value = Float.valueOf(splitString[1]);
                            amass += value;
                        }
                    }
                }
            } else {
                return 0;
            }

            if (x > 5) {
                return amass;
            } else if (x == 0) {
                return 0;
            } else if (amass > timeLimit) {
                float overallAchieved = (amass - timeLimit);
                Formatter formatter = new Formatter();
                formatter.format("%.2f", overallAchieved);
                String FT = formatter.toString();
                return Float.parseFloat(FT);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    Map<Boolean, Float> newConnectionAffectedP1(String p1, String date) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {
            LocalDate ldate = LocalDate.parse(date);
            Period period = Period.ofDays(-1);
            List<String> totalAsiggnedMeters;
            float totalAsiggnedMetersNumber = 0.0f;

            float connectedMeters = 0;
            if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p1, date) != null) {
                connectedMeters = slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED",
                        p1, date);
            }
            ;
            float activeMeters;
            String[] parts;

            if (slaDataEntryRepository.getNewConnectionTimeLimit(p1, date) != 0.0) {
                if (slaDataEntryRepository.getTotalAssignedMetersNewConnectionP1(date) != null) {
                    totalAsiggnedMeters = slaDataEntryRepository.getTotalAssignedMetersNewConnectionP1(date);
                    for (Integer a = 0; a < totalAsiggnedMeters.size(); a++) {
                        String str = totalAsiggnedMeters.get(a);
                        parts = str.split(",");
                        String extractDate = parts[0];
                        LocalDate dateCompare = LocalDate.parse(extractDate);
                        if (dateCompare.isBefore(ldate.plus(period)) || dateCompare.isEqual(ldate.plus(period))) {
                            Float value = Float.valueOf(parts[1]);
                            totalAsiggnedMetersNumber += value;
                        }
                    }

                    if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p1,
                            date) != null && totalAsiggnedMetersNumber != 0) {
                        activeMeters = (totalAsiggnedMetersNumber - connectedMeters);
                        ret.put(true, activeMeters);
                        return ret;
                    } else {
                        activeMeters = 0;
                        ret.put(true, activeMeters);
                        return ret;
                    }
                }
            } else {
                ret.put(true, 0.0f);
                return ret;
            }
            ret.put(false, 0.0f);
            return ret;
        } catch (Exception e) {
            ret.put(false, 0.0f);
            return ret;
        }
    }

    Map<Boolean, Float> newConnectionAffectedP2(String p2, String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {

            LocalDate ldate = LocalDate.parse(dmy);
            Period period = Period.ofDays(-1);
            String date = ldate.plus(period).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<String> totalAsiggnedMeters;
            float totalAsiggnedMetersNumber = 0.0f;
            float connectedMeters = 0;
            if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p2, date) != null) {
                connectedMeters = slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED",
                        p2, date);
            }
            ;
            float activeMeters;
            String[] parts;

            if (slaDataEntryRepository.getNewConnectionTimeLimit(p2, date) != 0.0) {
                if (slaDataEntryRepository.getTotalAssignedMetersNewConnectionP2(date) != null) {
                    totalAsiggnedMeters = slaDataEntryRepository.getTotalAssignedMetersNewConnectionP2(date);
                    for (Integer a = 0; a < totalAsiggnedMeters.size(); a++) {
                        String str = totalAsiggnedMeters.get(a);
                        parts = str.split(",");
                        String extractDate = parts[0];
                        LocalDate dateCompare = LocalDate.parse(extractDate);
                        if (dateCompare.isBefore(ldate.plus(period)) || dateCompare.isEqual(ldate.plus(period))) {
                            Float value = Float.valueOf(parts[1]);
                            totalAsiggnedMetersNumber += value;
                        }
                    }

                    if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p2,
                            date) != null && totalAsiggnedMetersNumber != 0) {
                        activeMeters = (totalAsiggnedMetersNumber - connectedMeters);
                        ret.put(true, activeMeters);
                        return ret;
                    } else {
                        ret.put(true, 0.0f);
                        return ret;
                    }
                }
            } else {
                ret.put(true, 0.0f);
                return ret;
            }
            ret.put(false, 0.0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0.0f);
            return ret;
        }
    }

    Map<Boolean, Float> newConnectionAffectedP3(String p3, String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {
            // date restart -1 days
            LocalDate ldate = LocalDate.parse(dmy);
            Period period = Period.ofDays(-1);
            String date = ldate.plus(period).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<String> totalAsiggnedMeters;
            float totalAsiggnedMetersNumber = 0.0f;
            float connectedMeters = 0;

            if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p3, date) != null) {
                connectedMeters = slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED",
                        p3, date);
            }
            float activeMeters;
            String[] parts;

            if (slaDataEntryRepository.getNewConnectionTimeLimit(p3, date) != 0.0) {
                if (slaDataEntryRepository.getTotalAssignedMetersNewConnectionP3(date) != null) {
                    totalAsiggnedMeters = slaDataEntryRepository.getTotalAssignedMetersNewConnectionP3(date);
                    for (Integer a = 0; a < totalAsiggnedMeters.size(); a++) {
                        String str = totalAsiggnedMeters.get(a);
                        parts = str.split(",");
                        String extractDate = parts[0];
                        LocalDate dateCompare = LocalDate.parse(extractDate);
                        if (dateCompare.isBefore(ldate.plus(period)) || dateCompare.isEqual(ldate.plus(period))) {
                            Float value = Float.valueOf(parts[1]);
                            totalAsiggnedMetersNumber += value;
                        }
                    }

                    if (slaDataMeterRepository.getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", p3,
                            date) != null && totalAsiggnedMetersNumber != 0) {
                        activeMeters = (totalAsiggnedMetersNumber - connectedMeters);
                        ret.put(true, activeMeters);
                        return ret;
                    } else {
                        ret.put(true, 0.0f);
                        return ret;
                    }
                }
            } else {
                ret.put(true, 0f);
                return ret;
            }
            ret.put(false, 0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0f);
            return ret;
        }
    }

    Map<Boolean, Float> ExceedingTheMaximumPlannedOutageHES(String dmy) {

        Map<Boolean, Float> ret = new HashMap<>();

        try {
            // year
            Date year = new SimpleDateFormat("yyyy-MM-dd").parse(dmy);
            Calendar c = Calendar.getInstance();
            c.setTime(year);
            Date dateYYYY = c.getTime();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String yearOnly = yearFormat.format(dateYYYY);

            ArrayList<String> plannedOutage;
            float amass = 0;
            float x = 0;
            float timeLimit = 0;
            if (slaDataEntryRepository.getTimeLimitPlannedOutageHES() != null) {
                timeLimit = slaDataEntryRepository.getTimeLimitPlannedOutageHES();
            }

            if (slaDataEntryRepository.getExceedingTheMaximumPlannedOutageOccasionsOrDuration() != null) {
                plannedOutage = slaDataEntryRepository.getExceedingTheMaximumPlannedOutageOccasionsOrDuration();
                for (Integer i = 0; i < plannedOutage.size(); i++) {
                    String[] splitString = plannedOutage.get(i).split(",");
                    if (splitString[0].contains(yearOnly)) {
                        ++x;
                        if (splitString[0].contains(dmy)) {
                            Float value = Float.valueOf(splitString[1]);
                            amass += value;
                        }
                    }
                }
            }

            if (x > 5) {
                ret.put(true, amass);
                return ret;
            } else if (x == 0) {
                ret.put(true, 0f);
                return ret;
            } else if (amass > timeLimit) {
                float overallAchieved = (amass - timeLimit);
                Formatter formatter = new Formatter();
                formatter.format("%.2f", overallAchieved);
                String FT = formatter.toString();
                float finalSL = Float.parseFloat(FT);
                ret.put(true, finalSL);
                return ret;
            }
            ret.put(true, 0f);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            ret.put(false, 0f);
            return ret;
        }
    }

}