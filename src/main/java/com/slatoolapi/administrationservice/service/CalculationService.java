package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.repository.SLADataMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class CalculationService {

    @Autowired
    private SLADataMeterRepository slaDataMeterRepository;

    public float ConfigurationofMeterforTimeofUse(String Px, String dmy) {

        float meterCount = slaDataMeterRepository
                .getMeterCountDtoSLA78("OnDemandSetTariffAgreement", dmy, "FINISH_OK", Px).floatValue();
        float totalMeter = slaDataMeterRepository.getTotalCountDtoSLA78("OnDemandSetTariffAgreement", dmy, Px)
                .floatValue();

        if (totalMeter > 0) {
            float SL = (meterCount / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));

        }

        return 100f;
    }

    public float ConfigurationofMeterforTimeofUseAffects(String Px, String dmy) {
        float metersAffects = slaDataMeterRepository.getMetersAffectsDtoSLA78(Px, dmy, "OnDemandSetTariffAgreement")
                .floatValue();
        return Float.valueOf(String.format(Locale.US, "%.2f", metersAffects));
    }

    public float ConfigurationofMeterforTimeofUseAffectsOverall(String P1, String P2, String P3, String dmy) {
        float metersAffects = slaDataMeterRepository
                .getMetersAffectsDtoSLA78Ov(P1, P2, P3, dmy, "OnDemandSetTariffAgreement").floatValue();
        return Float.valueOf(String.format(Locale.US, "%.2f", metersAffects));
    }

    public float ConfigurationofMeterforTimeofUseOverall(String P1, String P2, String P3, String dmy) {

        float meterCount = slaDataMeterRepository
                .getMeterCountDtoSLA78Ov("OnDemandSetTariffAgreement", dmy, "FINISH_OK", P1, P2, P3).floatValue();
        float totalMeter = slaDataMeterRepository.getTotalCountDtoSLA78Ov("OnDemandSetTariffAgreement", dmy, P1, P2, P3)
                .floatValue();

        if (totalMeter > 0) {
            float SL = (meterCount / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));

        }
        return 100;
    }

    public float NetworkAvailabilityHES(String Px, String dmy) {

        float activeMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityActiveMetersHES("ACTIVE", "DISCONNECTED", Px, dmy).floatValue();

        float baselineMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityBaseLineHES("ACTIVE", "UNREACHABLE", "DISCONNECTED", Px, dmy).floatValue();

        if (baselineMettersDto > 0) {
            float SL = (activeMettersDto / baselineMettersDto) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float NetworkAvailabilityOverallHES(String P1, String P2, String P3, String dmy) {

        float activeMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityActiveMetersOverallHES("ACTIVE", "DISCONNECTED", P1, P2, P3, dmy).floatValue();
        float baselineMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityBaseLineOverallHES("ACTIVE", "UNREACHABLE", "DISCONNECTED", P1, P2, P3, dmy)
                .floatValue();

        if (baselineMettersDto > 0) {
            float SL = (activeMettersDto / baselineMettersDto) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float NetworkAvailabilityAffectsHES(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository
                .getNetworkAvailabilityMetersAffectsHES("ACTIVE", "UNREACHABLE", "DISCONNECTED", dmy, Px).floatValue();

        if (metersAffectsDto > 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;

    }

    public float NetworkAvailabilityAffectsOverallHES(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository
                .getNetworkAvailabilityMetersAffectsOverallHES("ACTIVE", "UNREACHABLE", "DISCONNECTED", dmy, P1, P2, P3)
                .floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float NetworkAvailability(String Px, String dmy) {

        float activeMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityActiveMeters("ACTIVE", "DISCONNECTED", Px, dmy).floatValue();
        float baselineMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityBaseLine("ACTIVE", "UNREACHABLE", "DISCONNECTED", Px, dmy).floatValue();

        if (baselineMettersDto > 0) {
            float SL = (activeMettersDto / baselineMettersDto) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float NetworkAvailabilityOverall(String P1, String P2, String P3, String dmy) {

        float activeMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityActiveMetersOverall("ACTIVE", "DISCONNECTED", P1, P2, P3, dmy).floatValue();
        float baselineMettersDto = slaDataMeterRepository
                .getNetworkAvailabilityBaseLineOverall("ACTIVE", "UNREACHABLE", "DISCONNECTED", P1, P2, P3, dmy)
                .floatValue();

        if (baselineMettersDto > 0) {
            float SL = (activeMettersDto / baselineMettersDto) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float NetworkAvailabilityAffects(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository
                .getNetworkAvailabilityMetersAffects("ACTIVE", "UNREACHABLE", "DISCONNECTED", dmy, Px).floatValue();

        if (metersAffectsDto > 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;

    }

    public float NetworkAvailabilityAffectsOverall(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository
                .getNetworkAvailabilityMetersAffectsOverall("ACTIVE", "UNREACHABLE", "DISCONNECTED", dmy, P1, P2, P3)
                .floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;
    }

    public float SLA79Calculated(String Px, String dmy) {

        float meterCountDto = slaDataMeterRepository
                .getMeterCountDtoSLA79("OnDemandSetPaymentMode", "OnDemandGetPaymentMode", dmy, "FINISH_OK", Px)
                .floatValue();
        ;
        float totalMeterDto = slaDataMeterRepository
                .getTotalCountDtoSLA79("OnDemandSetPaymentMode", "OnDemandGetPaymentMode", dmy, Px).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float SLA79CalculatedAffects(String Px, String dmy) {
        float totalMeterDto = slaDataMeterRepository
                .getTotalCountDtoSLA79("OnDemandSetPaymentMode", "OnDemandGetPaymentMode", dmy, Px).floatValue();
        float metersAffectsDto = slaDataMeterRepository
                .getMetersAffectsDtoSLA79(Px, dmy, "OnDemandSetPaymentMode", "OnDemandGetPaymentMode").floatValue();

        if (totalMeterDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float SLA79CalculatedAffectsOv(String P1, String P2, String P3, String dmy) {
        float totalMeterDto = slaDataMeterRepository
                .getTotalCountDtoSLA79Ov("OnDemandSetPaymentMode", "OnDemandGetPaymentMode", dmy, P1, P2, P3)
                .floatValue();
        ;
        float metersAffectsDto = slaDataMeterRepository
                .getMetersAffectsDtoSLA79Ov(P1, P2, P3, dmy, "OnDemandSetPaymentMode", "OnDemandGetPaymentMode")
                .floatValue();
        ;

        if (totalMeterDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float SLA79CalculatedOverall(String P1, String P2, String P3, String dmy) {

        float meterCountDto = slaDataMeterRepository.getMeterCountDtoSLA79Ov("OnDemandSetPaymentMode",
                "OnDemandGetPaymentMode", dmy, "FINISH_OK", P1, P2, P3).floatValue();
        float totalMeterDto = slaDataMeterRepository
                .getTotalCountDtoSLA79Ov("OnDemandSetPaymentMode", "OnDemandGetPaymentMode", dmy, P1, P2, P3)
                .floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float SLA81Calculated(String Px, String dmy) {

        float meterCountDto = slaDataMeterRepository
                .getMeterCountDtoSLA81("OnDemandSetLoadLimitation", dmy, "FINISH_OK", Px).floatValue();
        float totalMeterDto = slaDataMeterRepository.getTotalCountDtoSLA81("OnDemandSetLoadLimitation", dmy, Px)
                .floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float SLA81CalculatedAffects(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getMetersAffectsDtoSLA81(Px, dmy, "OnDemandSetLoadLimitation")
                .floatValue();
        ;

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;
    }

    public float SLA81CalculatedAffectsOv(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository
                .getMetersAffectsDtoSLA81Ov(P1, P2, P3, dmy, "OnDemandSetLoadLimitation").floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;

    }

    public float SLA81CalculatedOverall(String P1, String P2, String P3, String dmy) {

        float meterCountDto = slaDataMeterRepository
                .getMeterCountDtoSLA81Ov("OnDemandSetLoadLimitation", dmy, "FINISH_OK", P1, P2, P3).floatValue();
        float totalMeterDto = slaDataMeterRepository
                .getTotalCountDtoSLA81Ov("OnDemandSetLoadLimitation", dmy, P1, P2, P3).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float AutomaticServiceRI(String Px, String dmy) {

        float meterCountDto = slaDataMeterRepository.getAutomaticServiceMeter("OnDemandDisconnect",
                "OnDemandSetLoadLimitation",
                "FINISH_OK", dmy, Px).floatValue();
        float totalMeterDto = slaDataMeterRepository.getAutomaticServiceTotal("OnDemandDisconnect",
                "OnDemandSetLoadLimitation",
                dmy, Px).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float AutomaticServiceRIAffects(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getAutomaticServiceAffects("OnDemandDisconnect",
                "OnDemandSetLoadLimitation", dmy, Px).floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;

    }

    public float AutomaticServiceRIOverall(String P1, String P2, String P3, String dmy) {

        float meterCountDto = slaDataMeterRepository.getAutomaticServiceM("OnDemandDisconnect",
                "OnDemandSetLoadLimitation",
                dmy, "FINISH_OK", P1, P2, P3).floatValue();
        float totalMeterDto = slaDataMeterRepository.getAutomaticServiceTotalM("OnDemandDisconnect",
                "OnDemandSetLoadLimitation",
                dmy, P1, P2, P3).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float AutomaticServiceRIOverallAffects(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getAutomaticServiceA("OnDemandDisconnect",
                "OnDemandSetLoadLimitation", dmy, P1, P2, P3).floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;
    }

    public float RemoteConnectDisconnectforSelectedConsumers(String Px, String dmy) {

        float meterCountDto = slaDataMeterRepository.getRemoteCDMeter("OnDemandDisconnect",
                "FINISH_OK", dmy, Px).floatValue();
        float totalMeterDto = slaDataMeterRepository.getRemoteCDTotal("OnDemandDisconnect",
                dmy, Px).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float RemoteCDAffects(String Px, String dmy) {
        float totalMeterDto = slaDataMeterRepository.getRemoteCDTotal("OnDemandDisconnect",
                dmy, Px).floatValue();
        float metersAffectsDto = slaDataMeterRepository.getRemoteCDAffects("OnDemandDisconnect",
                dmy, Px).floatValue();

        if (totalMeterDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }

        return 0;
    }

    public float RemoteCDServiceOverall(String P1, String P2, String P3, String dmy) {
        float meterCountDto = slaDataMeterRepository.getRemoteCDOMeter("OnDemandDisconnect",
                "FINISH_OK", dmy, P1, P2, P3).floatValue();
        float totalMeterDto = slaDataMeterRepository.getRemoteCDOTotal("OnDemandDisconnect",
                dmy, P1, P2, P3).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float RemoteCDOverallAffects(String P1, String P2, String P3, String dmy) {
        float totalMeterDto = slaDataMeterRepository.getRemoteCDOTotal("OnDemandDisconnect",
                dmy, P1, P2, P3).floatValue();
        float metersAffectsDto = slaDataMeterRepository.getRemoteCDOAffects("OnDemandDisconnect", dmy, P1, P2, P3)
                .floatValue();

        if (totalMeterDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float remoteDataAcquisition(String Px, String dmy) {

        float commandCountDto = slaDataMeterRepository.getCommandCount(
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
                "FINISH_OK", dmy, Px).floatValue();
        float totalCommandDto = slaDataMeterRepository.getTotalCommand(
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
                dmy, Px).floatValue();

        if (totalCommandDto > 0) {
            float SL = (commandCountDto / totalCommandDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float remoteDataAcquisitionAffects(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getRemoteDataAcquisitionAffects(
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
                dmy, Px).floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;

    }

    public float remoteDataAcquisitionOverall(String P1, String P2, String P3, String dmy) {

        float commandCountDto = slaDataMeterRepository.getCommandCountOverall(
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
                "FINISH_OK",
                dmy, P1, P2, P3).floatValue();
        float totalCommandDto = slaDataMeterRepository.getTotalCommandOverall2(
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
                dmy, P1, P2, P3).floatValue();

        if (totalCommandDto != 0) {
            float SL = (commandCountDto / totalCommandDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 0;
    }

    public float remoteDataAcquisitionOverallAffects(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getRemoteDataAcquisitionAffectsOverall(
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
                dmy, P1, P2, P3).floatValue();

        if (metersAffectsDto != 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;

    }

    public float remoteDataAcquisitionTolerance(String Px, String dmy) {

        float commandCountDto = slaDataMeterRepository.getRemoteDataAcquisitionTolerance(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                "FINISH_OK", dmy, Px).floatValue();
        float totalCommandDto = slaDataMeterRepository.getRemoteDataAcquisitionToleranceTotal(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                dmy, Px).floatValue();

        if (totalCommandDto > 0) {
            float SL = (commandCountDto / totalCommandDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float remoteDataAcquisitionAffectsTolerance(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getRemoteDataAcquisitionAffectsTolerance(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                dmy, Px).floatValue();

        if (metersAffectsDto != 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;

    }

    public float remoteDataAcquisitionOverallTolerance(String P1, String P2, String P3, String dmy) {

        float commandCountDto = slaDataMeterRepository.getRemoteDataAcquisitionOverallTolerance(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                "FINISH_OK",
                dmy, P1, P2, P3).floatValue();
        float totalCommandDto = slaDataMeterRepository.getRemoteDataAcquisitionTotalOverallTolerance(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                dmy, P1, P2, P3).floatValue();

        if (totalCommandDto > 0) {
            float SL = (commandCountDto / totalCommandDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 0;
    }

    public float remoteDataAcquisitionOverallAffectsTolerance(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getRemoteDataAcquisitionAffectsOverallTolerance(
                "OnDemandAbsoluteProfile",
                "OnDemandWaterProfile",
                dmy, P1, P2, P3).floatValue();

        if (metersAffectsDto != 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;

    }

    public float AlarmInDeviceUntilMessageReachesCustomerP1(String Px, Integer limitTimesP1) {
        // date restart -1 days
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP1(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP1).floatValue();

        float totalMeter = slaDataMeterRepository.getAlarmInDeviceTotal(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerP2(String Px, Integer limitTimesP2) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP2(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP2).floatValue();

        float totalMeter = slaDataMeterRepository.getAlarmInDeviceTotal(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerP3(String Px, Integer limitTimesP3) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP3(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP3).floatValue();
        float totalMeter = slaDataMeterRepository.getAlarmInDeviceTotal(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerOverall(String P1, String P2, String P3,
            Integer limitTimesOverall) {
        // date restart -1 days
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                P1, P2, P3, limitTimesOverall).floatValue();

        float totalMeter = slaDataMeterRepository.getAlarmInDeviceTotalOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                P1, P2, P3).floatValue();
        if (totalMeter > 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerP1Affected(String Px, String dmy, Integer limitTimesP1) {

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP1(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP1).floatValue();

        if (alarmsDevice > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", alarmsDevice));
        }

        return 0;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerP2Affected(String Px, String dmy, Integer limitTimesP2) {

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP2(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP2).floatValue();

        if (alarmsDevice > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", alarmsDevice));
        }

        return 0;
    }

    public float AlarmInDeviceUntilMessageReachesCustomerP3Affected(String Px, String dmy, Integer limitTimesP3) {

        float alarmsDevice = slaDataMeterRepository.getAlarmInDeviceMetersP3(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px, limitTimesP3).floatValue();

        if (alarmsDevice > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", alarmsDevice));
        }
        return 0;

    }

    public float AlarmInDeviceUntilMessageReachesCustomerOverallAffected(String P1, String P2, String P3, String dmy,
            Integer limitTimesOverall) {

        float affected = slaDataMeterRepository.getAlarmInDeviceMetersOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification", dmy, P1, P2, P3, limitTimesOverall).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float MeasurementOfPowerIndicators(String Px, String dmy) {

        float meterCountDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsMeters(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy,
                "FINISH_OK",
                Px).floatValue();

        float totalMeterDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsTotal(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy,
                Px).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MeasurementOfPowerIndicatorsAffects(String Px, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsAffects(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy,
                Px).floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float MeasurementOfPowerIndicatorsOverall(String P1, String P2, String P3, String dmy) {

        float meterCountDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsMetersOverall(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();

        float totalMeterDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsTotalOverall(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy, P1, P2, P3).floatValue();

        if (totalMeterDto > 0) {
            float SL = (meterCountDto / totalMeterDto) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float MeasurementOfPowerIndicatorsAffectsOverall(String P1, String P2, String P3, String dmy) {

        float metersAffectsDto = slaDataMeterRepository.getMeasurementOfPowerIndicatorsAffectsOverall(
                "OnDemandReadLoadProfile1",
                "OnDemandReadInstantaneousValues",
                dmy, P1, P2, P3).floatValue();

        if (metersAffectsDto > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", metersAffectsDto));
        }
        return 0;
    }

    public float RemotelyReadEventLogsPx(String Px, String dmy, Integer time_limit) {

        float alarmsDevice = slaDataMeterRepository.getRemotelyReadEventLogsPx(
                "OnDemandEventLog",
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getRemotelyReadEventLogsTotal(
                "OnDemandEventLog",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemotelyReadEventLogsOverall(String P1, String P2, String P3, String dmy, Integer time_limit) {

        float alarmsDevice = slaDataMeterRepository.getRemotelyReadEventLogsOverall(
                "OnDemandEventLog",
                dmy,
                "FINISH_OK",
                P1, P2, P3, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getRemotelyReadEventLogsTotalOverall(
                "OnDemandEventLog",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter != 0) {
            float SL = (alarmsDevice / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float RemotelyReadEventLogsAffectedPx(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemotelyReadEventLogsAffected(
                "OnDemandEventLog", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float RemotelyReadEventLogsAffectedOverall(String P1, String P2, String P3, String dmy) {
        float affected = slaDataMeterRepository.getRemotelyReadEventLogsAffectedOverall(
                dmy, P1, P2, P3, "OnDemandEventLog").floatValue();
        return Float.valueOf(String.format(Locale.US, "%.2f", affected));
    }

    public float RemotelyAlterSettingsPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemotelyAlterSettingsSuceedMeterPx(
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
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getRemotelyAlterSettingsTotal(
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
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MaxTransDelaySwitchCommandsPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getMaxTransDelaySwitchCommandsSuceedMeterPx(
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
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getMaxTransDelaySwitchCommandsTotalPX(
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
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MaxTransDelayUpdatesPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getMaxTransDelayUpdatesSuceedMeterPx(
                "OnDemandFirmwareUpgrade",
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getMaxTransDelayUpdatesTotalPX(
                "OnDemandFirmwareUpgrade",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemotelyAlterSettingsOverall(String P1, String P2, String P3, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemotelyAlterSettingsSuceedMeterOverall(
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
                dmy,
                "FINISH_OK",
                P1, P2, P3, time_limit).floatValue();

        float totalMeter = slaDataMeterRepository.getRemotelyAlterSettingsTotalOverall(
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
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float RemotelyAlterSettingsAffectedPx(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemotelyAlterSettingsAffected(
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
                dmy, Px).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));

    }

    public float MaxTransDelaySwitchCommandsAffectedPx(String Px, String dmy) {

        float affected = slaDataMeterRepository.getMaxTransDelaySwitchCommandsAffectedPx(
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
                dmy, Px).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));

    }

    public float MaxTransDelayUpdatesAffectedPx(String Px, String dmy) {

        float affected = slaDataMeterRepository.getMaxTransDelayUpdatesAffectedPx(
                "OnDemandFirmwareUpgrade",
                dmy, Px).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));

    }

    public float RemotelyAlterSettingsAffectedOverall(String P1, String P2, String P3, String dmy) {

        float affected = slaDataMeterRepository.getRemotelyAlterSettingsAffectedOverall(
                dmy,
                P1, P2, P3,
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
                "OnDemandChangeMeterPassword").floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));

    }

    public float MeterLossOfSupplyPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getMeterLossOfSupplySuceedMeterPx(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();

        float totalMeter = slaDataMeterRepository.getMeterLossOfSupplyTotal(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MeterLossOfSupplyOverall(String P1, String P2, String P3, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getMeterLossOfSupplySuceedMeterOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                "FINISH_OK",
                P1, P2, P3, time_limit).floatValue();

        float totalMeter = slaDataMeterRepository.getMeterLossOfSupplyTotalOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float MeterLossOfSupplyAffectPx(String Px, String dmy) {

        float affected = slaDataMeterRepository.getMeterLossOfSupplyAffected(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification", dmy, Px).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));

    }

    public float MeterLossOfSupplyAffectOverall(String P1, String P2, String P3, String dmy) {

        float affected = slaDataMeterRepository.getMeterLossOfSupplyAffectedOverall(
                "OnAlarmNotification",
                "OnConnectivityNotification",
                "OnPowerDownNotification",
                "OnInstallationNotification", dmy, P1, P2, P3).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float RemoteConnectDisconnectForSelectedConsumersPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersPx(
                "OnDemandDisconnect", "OnDemandConnect",
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersTotal(
                "OnDemandDisconnect", "OnDemandConnect",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteConnectDisconnectForSelectedConsumersOverall(String P1, String P2, String P3, String dmy,
            Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersOverall(
                "OnDemandDisconnect", "OnDemandConnect",
                dmy,
                "FINISH_OK",
                P1, P2, P3, time_limit).floatValue();
        float totalMeter = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersTotalOverall(
                "OnDemandDisconnect", "OnDemandConnect",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float RemoteConnectDisconnectForSelectedConsumersAffectedP1(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersAffected(
                "OnDemandDisconnect", "OnDemandConnect", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return affected;

    }

    public float RemoteConnectDisconnectForSelectedConsumersAffectedP2(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersAffected(
                "OnDemandDisconnect", "OnDemandConnect", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float RemoteConnectDisconnectForSelectedConsumersAffectedP3(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersAffected(
                "OnDemandDisconnect", "OnDemandConnect", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float RemoteConnectDisconnectForSelectedConsumersAffectedOverall(String P1, String P2, String P3,
            String dmy) {

        float affected = slaDataMeterRepository.getRemoteConnectDisconnectForSelectedConsumersAffectedOverall(
                dmy, P1, P2, P3,
                "OnDemandDisconnect", "OnDemandConnect").floatValue();

        if (affected >= 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;
    }

    public float ReadTheEventLogsPertainingToAllMeters(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemoteLoadControlCommandsPertaining(
                "OnDemandEventLog",
                dmy,
                "FINISH_OK",
                Px, time_limit).floatValue();

        float totalMeter = slaDataMeterRepository.getRemoteLoadControlCommandsTotalPx(
                "OnDemandEventLog",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float ReadTheEventLogsPertainingToAllMetersAffected(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteLoadControlCommandsAffected(
                "OnDemandEventLog", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float RemoteLoadControlCommandsPx(String Px, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemoteLoadControlCommandsPx(
                "OnDemandSetLoadLimitation", "OnDemandGetLoadLimitThreshold",
                dmy,
                "FINISH_OK", time_limit,
                Px).floatValue();

        float totalMeter = slaDataMeterRepository.getRemoteLoadControlCommandsTotalNew(
                "OnDemandSetLoadLimitation", "OnDemandGetLoadLimitThreshold",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteLoadControlCommandsOverallNew(String P1, String P2, String P3, String dmy, Integer time_limit) {

        float suceedMeter = slaDataMeterRepository.getRemoteLoadControlCommandsOverallNew(
                "OnDemandSetLoadLimitation", "OnDemandGetLoadLimitThreshold",
                dmy,
                "FINISH_OK", time_limit,
                P1, P2, P3).floatValue();
        float totalMeter = slaDataMeterRepository.getRemoteLoadControlCommandsTotalOverallNew(
                "OnDemandSetLoadLimitation", "OnDemandGetLoadLimitThreshold",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;
    }

    public float RemoteLoadControlCommandsAffectedPx(String Px, String dmy) { // new

        float affected = slaDataMeterRepository.getRemoteLoadControlCommandsAffectedNew(
                "OnDemandSetLoadLimitation", "OnDemandGetLoadLimitThreshold", dmy, Px).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", affected));
    }

    public float RemoteLoadControlCommandsAffectedOverall(String P1, String P2, String P3, String dmy) {

        float totalMeter = slaDataMeterRepository.getRemoteLoadControlCommandsTotalOverall(
                "OnDemandSetLoadLimitation",
                dmy,
                P1, P2, P3).floatValue();

        float affected = slaDataMeterRepository.getRemoteLoadControlCommandsAffectedOverall(
                "OnDemandSetLoadLimitation", dmy, P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;
    }

    public float ReconnectionsFromCustomerP1(String Px) {
        // date restart -1 days
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP1(
                "OnDemandCutOffReconnection",
                dmy,
                "FINISH_OK",
                Px).floatValue();
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerP2(String Px) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP2(
                "OnDemandCutOffReconnection",
                dmy,
                "FINISH_OK",
                Px).floatValue();
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerP3(String Px) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP3(
                "OnDemandCutOffReconnection",
                dmy,
                "FINISH_OK",
                Px).floatValue();
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerOverall(String P1, String P2, String P3) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerOverall(
                "OnDemandCutOffReconnection",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotalOverall(
                "OnDemandCutOffReconnection", dmy, P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerAffectedP1(String Px) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);

        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();
        float affected = slaDataMeterRepository.getReconnectionsFromCustomerAffected(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerAffectedP2(String Px) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();
        float affected = slaDataMeterRepository.getReconnectionsFromCustomerAffected(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerAffectedP3(String Px) {
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, -1);
        Date date = c.getTime();
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dmy = dmyFormat.format(date);
        float totalMeter = slaDataMeterRepository.getReconnectionsFromCustomerTotal(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();
        float affected = slaDataMeterRepository.getReconnectionsFromCustomerAffected(
                "OnDemandCutOffReconnection", dmy, Px).floatValue();

        if (totalMeter > 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;
    }

    public float ReconnectionsFromCustomerP1Affects(String Px, String dmy, Integer limitTimesP1) {

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP1Affects(
                "OnDemandConnect",
                dmy,
                Px, limitTimesP1);

        if (suceedMeter != 0) {

            return Float.valueOf(String.format(Locale.US, "%.2f", suceedMeter));
        }
        return 0;

    }

    public float ReconnectionsFromCustomerP2Affects(String Px, String dmy, Integer limitTimesP2) {

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP2Affects(
                "OnDemandConnect",
                dmy,
                Px, limitTimesP2).floatValue();

        if (suceedMeter != 0) {
            float SL = suceedMeter;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;

    }

    public float ReconnectionsFromCustomerP3Affects(String Px, String dmy, Integer limitTimesP3) {

        float suceedMeter = slaDataMeterRepository.getReconnectionsFromCustomerP3Affects(
                "OnDemandConnect",
                dmy,
                Px, limitTimesP3).floatValue();

        if (suceedMeter != 0) {
            float SL = suceedMeter;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 0;

    }

    public float ReconnectionsFromCustomerAffectedOverall(String P1, String P2, String P3, String dmy,
            Integer limitTimesOverall) {

        float affected = slaDataMeterRepository.getReconnectionsFromCustomerAffectedOverall(
                dmy, P1, P2, P3,
                "OnDemandConnect", limitTimesOverall).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float IntegrationOfDERPx(String Px, String dmy) {

        float suceedMeter = slaDataMeterRepository.getIntegrationOfDERMeterCount(
                "SLA32",
                dmy,
                "FINISH_OK",
                Px).floatValue();

        float totalMeter = slaDataMeterRepository.getIntegrationOfDERTotalMeters(
                "SLA32",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float IntegrationOfDERPxOverall(String P1, String P2, String P3, String dmy) {

        float suceedMeter = slaDataMeterRepository.getIntegrationOfDERMeterCountOverall(
                "SLA32",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();
        float totalMeter = slaDataMeterRepository.getIntegrationOfDERTotalMetersOverall(
                "SLA32",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float IntegrationOfDERPxAffects(String Px, String dmy) {

        float affected = slaDataMeterRepository.getIntegrationOfDERAffected(
                "SLA32", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float IntegrationOfDERPxAffectsOverall(String P1, String P2, String P3, String dmy) {

        float affected = slaDataMeterRepository.getIntegrationOfDERAffectedOverall(
                "SLA32", dmy, P1, P2, P3).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }

        return 100;

    }

    public float RemoteApplianceControlOnDemandPx(String Px, String dmy) {

        float suceedMeter = slaDataMeterRepository.getRemoteApplianceControlOnDemand(
                "SLA33",
                dmy,
                "FINISH_OK",
                Px).floatValue();

        float totalMeter = slaDataMeterRepository.getRemoteApplianceControlOnDemandTotalMeters(
                "SLA33",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteApplianceControlOnDemandOverall(String P1, String P2, String P3, String dmy) {

        float suceedMeter = slaDataMeterRepository.getRemoteApplianceControlOnDemandOverall(
                "SLA33",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();

        float totalMeter = slaDataMeterRepository.getRemoteApplianceControlOnDemandTotalMetersOverall(
                "SLA33",
                dmy,
                P1, P2, P3).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteApplianceControlOnDemandAffects(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteApplianceControlOnDemandAffected(
                "SLA33", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float RemoteApplianceControlOnDemandAffectsOverall(String P1, String P2, String P3, String dmy) {

        float affected = slaDataMeterRepository.getRemoteApplianceControlOnDemandAffectedOverall(
                "SLA33", dmy, P1, P2, P3).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float RemoteControlOfDecentralizedGenerationUpToShutDownPx(String Px, String dmy) {

        float suceedMeter = slaDataMeterRepository.getRemoteControlOfDecentralizedGenerationUpToShutDownMetersCount(
                "SLA34",
                dmy,
                "FINISH_OK",
                Px).floatValue();

        float totalMeter = slaDataMeterRepository.getRemoteControlOfDecentralizedGenerationUpToShutDownTotalMeters(
                "SLA34",
                dmy,
                Px).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteControlOfDecentralizedGenerationUpToShutDownOverall(String P1, String P2, String P3,
            String dmy) {

        float suceedMeter = slaDataMeterRepository.getRemoteControlOfDecentralizedGenerationUpToShutDownOverall(
                "SLA34",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();
        float totalMeter = slaDataMeterRepository
                .getRemoteControlOfDecentralizedGenerationUpToShutDownTotalMetersOverall(
                        "SLA34",
                        dmy,
                        P1, P2, P3)
                .floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float RemoteControlOfDecentralizedGenerationUpToShutDownAffects(String Px, String dmy) {

        float affected = slaDataMeterRepository.getRemoteControlOfDecentralizedGenerationUpToShutDownAffected(
                "SLA34", dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float RemoteControlOfDecentralizedGenerationUpToShutDownAffectsOverall(String P1, String P2, String P3,
            String dmy) {

        float affected = slaDataMeterRepository.getRemoteControlOfDecentralizedGenerationUpToShutDownAffectedOverall(
                "SLA34", dmy, P1, P2, P3).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float MeterAlarmsLoggedEvents(String Px, String dmy) {

        float suceedMeter = slaDataMeterRepository.getMeterAlarmsLoggedEventsMeterCount(
                "OnDemandEventLog",
                dmy,
                "FINISH_OK",
                Px).floatValue();

        float totalMeter = slaDataMeterRepository.getAlarmsloggedeventsTotalMeters(
                "OnDemandEventLog", Px, dmy).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MeterAlarmsLoggedEventsOverall(String P1, String P2, String P3, String dmy) {

        float suceedMeter = slaDataMeterRepository.getMeterAlarmsLoggedEventsMeterCountOverall(
                "OnDemandEventLog",
                dmy,
                "FINISH_OK",
                P1, P2, P3).floatValue();

        float totalMeter = slaDataMeterRepository.getAlarmsloggedeventsTotalMetersOverall(
                "OnDemandEventLog", P1, P2, P3, dmy).floatValue();

        if (totalMeter > 0) {
            float SL = (suceedMeter / totalMeter) * 100f;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float MeterAlarmsLoggedEventsAffects(String Px, String dmy) {

        float affected = slaDataMeterRepository.getAlarmsloggedeventsTotalMeters(
                "OnDemandEventLog", Px, dmy).floatValue();

        if (affected != 100) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float MeterAlarmsLoggedEventsAffectsOverall(String P1, String P2, String P3, String dmy) {

        float affected = slaDataMeterRepository.getAlarmsloggedeventsTotalMetersOverall(
                "OnDemandEventLog", P1, P2, P3, dmy).floatValue();

        if (affected != 100) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 100;

    }

    public float CollectionOfDailyMeterIntervalReadingHESAffectsEnergy(String Px, String dmy) {

        float affected = slaDataMeterRepository.getEnergyAffectsCount(
                dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float CollectionOfDailyMeterIntervalReadingHESAffectsWater(String Px, String dmy) {

        float affected = slaDataMeterRepository.getWaterAffectsCount(
                dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float IndividualReadHESAffectsWater(String Px, String dmy) {

        float affected = slaDataMeterRepository.getWaterAffectsCount(
                dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float IndividualReadHESAffectsEnergy(String Px, String dmy) {

        float affected = slaDataMeterRepository.getEnergyAffectsCount(
                dmy, Px).floatValue();

        if (affected != 0) {
            return Float.valueOf(String.format(Locale.US, "%.2f", affected));
        }
        return 0;

    }

    public float CollectionOfDailyMeterIntervalReadingHESAffects(String Px, String dmy) {

        float affectsEnergy = CollectionOfDailyMeterIntervalReadingHESAffectsEnergy(Px, dmy);
        float affectsWater = CollectionOfDailyMeterIntervalReadingHESAffectsWater(Px, dmy);

        if (affectsEnergy == 0 && affectsWater == 0) {
            return 0;
        } else {
            return (affectsEnergy + affectsWater);
        }
    }

    public float CollectionOfDailyMeterIntervalReadingHESPx(String Px, String dmy, Integer limitTimes) {

        Integer waterCount = slaDataMeterRepository
                .getWaterEnergyCountPnew("WaterProfile", dmy, "WATER", limitTimes, 24, "FINISH_OK", Px).size();
        Integer energyRegisterCount = slaDataMeterRepository
                .getWaterEnergyCountPnew("EnergyProfile", dmy, "ELECTRICITY", limitTimes, 24, "FINISH_OK", Px).size();

        float affectsEnergy = CollectionOfDailyMeterIntervalReadingHESAffectsEnergy(Px, dmy);
        float affectsWater = CollectionOfDailyMeterIntervalReadingHESAffectsWater(Px, dmy);

        if (affectsEnergy == 0 && affectsWater == 0) {
            return 0;
        }
        if (waterCount == 0 && energyRegisterCount == 0) {
            return 0;
        }
        float sl = ((waterCount + energyRegisterCount) / (affectsWater + affectsEnergy)) * 100;
        return Float.valueOf(String.format(Locale.US, "%.2f", sl));

    }

    public float CollectionOfDailyMeterIntervalReadingHES(String Px, String dmy, Integer limitTimes) {
    	//ADDC-3561 
        Integer waterCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL("WaterProfile", dmy, limitTimes, 1, "FINISH_OK", Px, "WATER");
        Integer energyCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL("EnergyProfile", dmy, limitTimes, 1, "FINISH_OK", Px, "ELECTRICITY");
        /*Integer maxDemadProfCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL("MaxDemandProfile", dmy, limitTimes, 24, "FINISH_OK", Px, "ELECTRICITY")
                .size();
        Integer billingCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL("BillingProfile", dmy, limitTimes, 24, "FINISH_OK", Px, "ELECTRICITY")
                .size();
        Integer instantaneousValuesCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL("InstantaneousValues", dmy, limitTimes, 24, "FINISH_OK", Px, "ELECTRICITY")
                .size();
      */
        float affectsEnergy = CollectionOfDailyMeterIntervalReadingHESAffectsEnergy(Px, dmy);
        float affectsWater = CollectionOfDailyMeterIntervalReadingHESAffectsWater(Px, dmy);

        if (affectsEnergy == 0 && affectsWater == 0) {
            return 0;
        }

        float sl = ((energyCount + waterCount )//+ maxDemadProfCount + billingCount + instantaneousValuesCount)
                / (affectsWater + affectsEnergy)) * 100;
        return Float.valueOf(String.format(Locale.US, "%.2f", sl));

    }

    public float IndividualReadHESAffects(String Px, String dmy) {

        float affectedWater = IndividualReadHESAffectsWater(Px, dmy);
        float affectedEnergy = IndividualReadHESAffectsEnergy(Px, dmy);
        float affects = affectedWater + affectedEnergy;

        if (affects == 0) {
            return 0;
        } else {
            return affects;
        }
    }

    public float IndividualReadHESPx(String Px, String dmy, Integer limitTimes) {

        float affects = IndividualReadHESAffects(Px, dmy);
   //TODO
        Integer waterCount = slaDataMeterRepository
                .getWaterEnergyCountPnewSL17("LoadProfile1", dmy, limitTimes, 1, "FINISH_OK", Px).size();

        float sl = (waterCount / affects) * 100;
        return Float.valueOf(String.format(Locale.US, "%.2f", sl));

    }

    public float SpatialAvailabilityHES(String zone, String Px, String dmy) {

        float activeMettersDto = slaDataMeterRepository
                .getSpatialAvailabilityHesActive("ACTIVE", "DISCONNECTED", Px, zone, dmy).floatValue();
        float baselineMettersDto = slaDataMeterRepository
                .getSpatialAvailabilityHesBaseline("ACTIVE", "UNREACHABLE", "DISCONNECTED", Px, zone, dmy).floatValue();

        if (baselineMettersDto > 0) {
            float SL = (activeMettersDto / baselineMettersDto) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }

        return 100;
    }

    public float SpatialAvailabilityHESOverall(String zone, String P1, String P2, String P3, String dmy) {

        float activeMetters = slaDataMeterRepository
                .getSpatialAvailabilityHesActiveOverall("ACTIVE", "DISCONNECTED", P1, P2, P3, zone, dmy).floatValue();
        float baselineMetters = slaDataMeterRepository.getSpatialAvailabilityHesBaselineOverall("ACTIVE", "UNREACHABLE",
                "DISCONNECTED", P1, P2, P3, zone, dmy).floatValue();

        if (baselineMetters > 0) {
            float SL = (activeMetters / baselineMetters) * 100F;
            return Float.valueOf(String.format(Locale.US, "%.2f", SL));
        }
        return 100;
    }

    public float SpatialAvailabilityHESAffects(String zone, String Px, String dmy) {

        float baselineMettersDto = slaDataMeterRepository.getSpatialAvailabilityHesBaseline("ACTIVE", "UNREACHABLE",
                "DISCONNECTED", Px, zone, dmy).floatValue();

        return Float.valueOf(String.format(Locale.US, "%.2f", baselineMettersDto));

    }

    public float SpatialAvailabilityHESOverallAffects(String zone, String P1, String P2, String P3, String dmy) {

        float baselineMettersDto = (float) (slaDataMeterRepository.getSpatialAvailabilityHesBaselineOverall("ACTIVE",
                "UNREACHABLE",
                "DISCONNECTED", P1, P2, P3, zone, dmy));
        return Float.valueOf(String.format(Locale.US, "%.2f", baselineMettersDto));

    }

    public float ReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(String px, String dmy, String comandos,
            String estado) {

        var resultDTO = slaDataMeterRepository.getReconnectionsFromCustomerCareCenterToEnergySupplyTolerance(dmy, px,
                comandos, estado);

        if (resultDTO != null) {
            return Float.valueOf(String.format(Locale.US, "%.2f", resultDTO));
        } else {
            return 0;
        }
    }

    public float ReconnectionsFromCustomerCareCenterToEnergySupplyToleranceOverall(String p1, String p2, String p3,
            String dmy, String comandos, String estado) {

        var resultDTO = slaDataMeterRepository.getReconnectionsFromCustomerCareCenterToEnergySupplyToleranceOverall(dmy,
                p1, p2, p3, comandos, estado);

        if (resultDTO != null) {

            return Float.valueOf(String.format(Locale.US, "%.2f", resultDTO));

        }
        return 0;

    }
}