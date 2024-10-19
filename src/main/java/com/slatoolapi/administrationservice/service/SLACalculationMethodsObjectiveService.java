package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.SLACalculationMethodObjectiveDto;
import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsObjectiveRepository;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLACalculationMethodsObjectiveService {

    @Autowired
    private SLACalculationMethodsObjectiveRepository slaCalculationMethodsObjectiveRepository;

    @Autowired
    private SLACalculationMethodsRepository slaCalculationMethodsRepository;

    public List<SLACalculationMethodObjectiveDto> getAll() {
        List<SLACalculationMethodsObjective> slaCalculationMethodsObjectives = slaCalculationMethodsObjectiveRepository
                .findAll();

        return slaCalculationMethodsObjectives.stream().map(this::mapDto).collect(Collectors.toList());
    }

    public SLACalculationMethodObjectiveDto save(SLACalculationMethodsObjective slaCalculationMethodsObjective) {

        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                .findById(slaCalculationMethodsObjective.getCalculationMethodsId()).orElse(null);
        assert slaCalculationMethods != null;

        SLACalculationMethodsObjective slaCalculationMethodsObjectiveBuild = SLACalculationMethodsObjective.builder()
                .calculationMethodsId(slaCalculationMethods.getId()).type(slaCalculationMethodsObjective.getType())
                .target(slaCalculationMethodsObjective.getTarget())
                .targetUOM(slaCalculationMethodsObjective.getTargetUOM())
                .compare(slaCalculationMethodsObjective.getCompare())
                .limitTimes(slaCalculationMethodsObjective.getLimitTimes())
                .numberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes())
                .evaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes())
                .evaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM())
                .consecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes())
                .consecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM()).build();

        SLACalculationMethodsObjective slaCalculationMethodsObjectiveNew = slaCalculationMethodsObjectiveRepository
                .save(slaCalculationMethodsObjectiveBuild);

        return mapDto(slaCalculationMethodsObjectiveNew);
    }

    public SLACalculationMethodObjectiveDto getById(long id) {
        SLACalculationMethodsObjective slaCalculationMethodsObjective = slaCalculationMethodsObjectiveRepository
                .findById(id).orElse(null);
        if (slaCalculationMethodsObjective != null) {
            return mapDto(slaCalculationMethodsObjective);
        }
        return null;
    }

    public SLACalculationMethodObjectiveDto update(long id,
            SLACalculationMethodsObjective slaCalculationMethodsObjective) {
        SLACalculationMethodsObjective slaCalculationMethodsObjectiveUpdated = slaCalculationMethodsObjectiveRepository
                .findById(id).orElse(null);
        if (slaCalculationMethodsObjectiveUpdated == null) {
            return null;
        }
        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                .findById(slaCalculationMethodsObjective.getCalculationMethodsId()).orElse(null);
        if (slaCalculationMethods == null) {
            return null;
        }

        slaCalculationMethodsObjectiveUpdated.setCalculationMethodsId(slaCalculationMethods.getId());
        slaCalculationMethodsObjectiveUpdated.setType(slaCalculationMethodsObjective.getType());
        slaCalculationMethodsObjectiveUpdated.setTarget(slaCalculationMethodsObjective.getTarget());
        slaCalculationMethodsObjectiveUpdated.setTargetUOM(slaCalculationMethodsObjective.getTargetUOM());
        slaCalculationMethodsObjectiveUpdated.setCompare(slaCalculationMethodsObjective.getCompare());
        slaCalculationMethodsObjectiveUpdated.setLimitTimes(slaCalculationMethodsObjective.getLimitTimes());
        slaCalculationMethodsObjectiveUpdated.setNumberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes());
        slaCalculationMethodsObjectiveUpdated.setEvaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes());
        slaCalculationMethodsObjectiveUpdated
                .setEvaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM());
        slaCalculationMethodsObjectiveUpdated.setConsecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes());
        slaCalculationMethodsObjectiveUpdated
                .setConsecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM());
        slaCalculationMethodsObjectiveRepository.save(slaCalculationMethodsObjectiveUpdated);
        return mapDto(slaCalculationMethodsObjectiveUpdated);

    }

    public boolean delete(long id) {
        SLACalculationMethodsObjective slaCalculationMethodsObjective = slaCalculationMethodsObjectiveRepository
                .findById(id).orElse(null);
        if (slaCalculationMethodsObjective != null) {
            slaCalculationMethodsObjectiveRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public SLACalculationMethodObjectiveDto mapDto(SLACalculationMethodsObjective slaCalculationMethodsObjective) {

        SLACalculationMethodObjectiveDto dto = new SLACalculationMethodObjectiveDto();
        dto.setId(slaCalculationMethodsObjective.getId());
        dto.setType(slaCalculationMethodsObjective.getType());
        dto.setTarget(slaCalculationMethodsObjective.getTarget());
        dto.setTargetUOM(slaCalculationMethodsObjective.getTargetUOM());
        dto.setCompare(slaCalculationMethodsObjective.getCompare());
        dto.setLimitTimes(slaCalculationMethodsObjective.getLimitTimes());
        dto.setNumberOfTimes(slaCalculationMethodsObjective.getNumberOfTimes());
        dto.setEvaluationTimes(slaCalculationMethodsObjective.getEvaluationTimes());
        dto.setEvaluationTimesUOM(slaCalculationMethodsObjective.getEvaluationTimesUOM());
        dto.setConsecutiveTimes(slaCalculationMethodsObjective.getConsecutiveTimes());
        dto.setConsecutiveTimesUOM(slaCalculationMethodsObjective.getConsecutiveTimesUOM());
        return dto;
    }

    public SLACalculationMethodsObjective getByCalculationMethodsIdAndType(Long id, String px) {
        return slaCalculationMethodsObjectiveRepository.getByCalculationMethodsIdAndType(id, px);

    }
}
