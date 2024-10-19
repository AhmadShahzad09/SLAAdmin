package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.SLACalculationModuleDto;
import com.slatoolapi.administrationservice.entity.SLACalculationModule;
import com.slatoolapi.administrationservice.repository.SLACalculationModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLACalculationModuleService {

    @Autowired
    private SLACalculationModuleRepository slaCalculationModuleRepository;

    public SLACalculationModuleDto save(SLACalculationModule slaCalculationModule) {

        SLACalculationModule slaCalculationModuleBuild = SLACalculationModule.builder()
                .name(slaCalculationModule.getName()).build();

        SLACalculationModule slaCalculationModuleNew = slaCalculationModuleRepository.save(slaCalculationModuleBuild);

        return mapDto(slaCalculationModuleNew);
    }

    public List<SLACalculationModuleDto> getAll() {
        List<SLACalculationModule> slaCalculationModules = slaCalculationModuleRepository.findAll();

        return slaCalculationModules.stream().map(calculationModule -> mapDto(calculationModule))
                .collect(Collectors.toList());
    }

    public SLACalculationModuleDto getById(long id) {
        SLACalculationModule slaCalculationModule = slaCalculationModuleRepository.findById(id).orElse(null);
        if (slaCalculationModule != null) {
            return mapDto(slaCalculationModule);
        }
        return null;
    }

    public SLACalculationModuleDto update(long id, SLACalculationModule slaCalculationModule) {
        SLACalculationModule slaCalculationModuleUpdated = slaCalculationModuleRepository.findById(id).orElse(null);
        if (slaCalculationModuleUpdated != null) {

            slaCalculationModuleUpdated.setName(slaCalculationModuleUpdated.getName());

            slaCalculationModuleRepository.save(slaCalculationModuleUpdated);

            return mapDto(slaCalculationModuleUpdated);
        }
        return null;
    }

    public boolean delete(long id) {
        SLACalculationModule slaCalculationModule = slaCalculationModuleRepository.findById(id).orElse(null);
        if (slaCalculationModule != null) {
            slaCalculationModuleRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public SLACalculationModuleDto mapDto(SLACalculationModule slaCalculationModule) {

        SLACalculationModuleDto dto = new SLACalculationModuleDto();
        dto.setId(slaCalculationModule.getId());
        dto.setName(slaCalculationModule.getName());

        return dto;
    }
}
