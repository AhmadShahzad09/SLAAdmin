package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.*;
import com.slatoolapi.administrationservice.entity.*;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsRepository;
import com.slatoolapi.administrationservice.repository.SLAScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLAScheduleService {

    @Autowired
    private SLACalculationMethodsRepository slaCalculationMethodsRepository;

    @Autowired
    private SLAScheduleRepository slaScheduleRepository;

    @Autowired
    private SLACalculationMethodsService slaCalculationMethodsService;

    public SLAScheduleDto save(SLASchedule slaSchedule) {

        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                .findById(slaSchedule.getCalculationMethodsId()).orElse(null);
        if (slaCalculationMethods == null) {
            return null;
        }

        SLASchedule slaScheduleBuild = SLASchedule.builder().description(slaSchedule.getDescription())
                .active(slaSchedule.isActive()).frecuency(slaSchedule.getFrecuency())
                .executionTime(slaSchedule.getExecutionTime()).lastExecution(slaSchedule.getLastExecution())
                .calculationMethodsId(slaCalculationMethods.getId()).build();

        SLASchedule slaScheduleNew = slaScheduleRepository.save(slaScheduleBuild);

        return mapDto(slaScheduleNew);
    }

    public List<SLAScheduleDto> getAll() {
        List<SLASchedule> slaSchedules = slaScheduleRepository.findAll();

        return slaSchedules.stream().map(SLASchedule -> mapDto(SLASchedule)).collect(Collectors.toList());
    }

    public SLAScheduleDto getById(long id) {
        SLASchedule slaSchedule = slaScheduleRepository.findById(id).orElse(null);
        if (slaSchedule != null) {
            return mapDto(slaSchedule);
        }
        return null;
    }

    public SLAScheduleDto update(long id, SLASchedule slaSchedule) {
        SLASchedule slaScheduleUpdated = slaScheduleRepository.findById(id).orElse(null);
        if (slaScheduleUpdated == null) {
            return null;
        }
        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                .findById(slaSchedule.getCalculationMethodsId()).orElse(null);
        if (slaCalculationMethods == null) {
            return null;
        }

        slaScheduleUpdated.setDescription(slaSchedule.getDescription());
        slaScheduleUpdated.setActive(slaSchedule.isActive());
        slaScheduleUpdated.setFrecuency(slaSchedule.getFrecuency());
        slaScheduleUpdated.setExecutionTime(slaSchedule.getExecutionTime());
        slaScheduleUpdated.setLastExecution(slaSchedule.getLastExecution());
        slaScheduleUpdated.setCalculationMethodsId(slaSchedule.getCalculationMethodsId());

        slaScheduleRepository.save(slaSchedule);

        return mapDto(slaScheduleUpdated);
    }

    public boolean delete(long id) {
        SLASchedule slaSchedule = slaScheduleRepository.findById(id).orElse(null);
        if (slaSchedule != null) {
            slaScheduleRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public SLAScheduleDto mapDto(SLASchedule slaSchedule) {

        SLACalculationMethods slaCalculationMethods = slaCalculationMethodsRepository
                .findById(slaSchedule.getCalculationMethodsId()).orElse(null);
        if (slaSchedule == null) {
            return null;
        }

        SLACalculationMethodsDto calculationModuleDto = slaCalculationMethodsService.mapDto(slaCalculationMethods);

        SLAScheduleDto dto = new SLAScheduleDto();
        dto.setId(slaSchedule.getId());
        dto.setDescription(slaSchedule.getDescription());
        dto.setActive(slaSchedule.isActive());
        dto.setFrecuency(slaSchedule.getFrecuency());
        dto.setExecutionTime(slaSchedule.getExecutionTime());
        dto.setLastExecution(slaSchedule.getLastExecution());
        dto.setCalculationMethodsId(calculationModuleDto);

        return dto;
    }
}
