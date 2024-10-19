package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.SLADataTypeDto;
import com.slatoolapi.administrationservice.entity.SLADataType;
import com.slatoolapi.administrationservice.repository.SLADataTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLADataTypeService {

    @Autowired
    private SLADataTypeRepository slaDataTypeRepository;

    public SLADataTypeDto save(SLADataType slaDataType) {

        SLADataType slaDataTypeBuild = SLADataType.builder().name(slaDataType.getName()).build();

        SLADataType slaDataTypeNew = slaDataTypeRepository.save(slaDataTypeBuild);

        return mapDto(slaDataTypeNew);
    }

    public List<SLADataTypeDto> getAll() {
        List<SLADataType> slaDataTypes = slaDataTypeRepository.findAll();

        return slaDataTypes.stream().map(dataType -> mapDto(dataType)).collect(Collectors.toList());
    }

    public SLADataTypeDto getById(long id) {
        SLADataType SLADataType = slaDataTypeRepository.findById(id).orElse(null);
        if (SLADataType != null) {
            return mapDto(SLADataType);
        }
        return null;
    }

    public SLADataTypeDto update(long id, SLADataType slaDataType) {

        SLADataType slaDataTypeUpdated = slaDataTypeRepository.findById(id).orElse(null);
        if (slaDataType != null) {

            slaDataTypeUpdated.setName(slaDataType.getName());

            slaDataTypeRepository.save(slaDataType);

            return mapDto(slaDataTypeUpdated);
        }
        return null;
    }

    public boolean delete(long id) {
        SLADataType SLADataType = slaDataTypeRepository.findById(id).orElse(null);
        if (SLADataType != null) {
            slaDataTypeRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public SLADataTypeDto mapDto(SLADataType slaDataType) {

        SLADataTypeDto dto = new SLADataTypeDto();
        dto.setId(slaDataType.getId());
        dto.setName(slaDataType.getName());

        return dto;
    }
}
