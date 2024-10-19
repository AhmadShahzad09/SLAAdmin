package com.slatoolapi.administrationservice.service;

import com.slatoolapi.administrationservice.dto.SLADataEntryDto;
import com.slatoolapi.administrationservice.dto.SLADataTypeDto;
import com.slatoolapi.administrationservice.entity.SLADataEntry;
import com.slatoolapi.administrationservice.entity.SLADataType;
import com.slatoolapi.administrationservice.repository.SLADataEntryRepository;
import com.slatoolapi.administrationservice.repository.SLADataTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SLADataEntryService {

    @Autowired
    private SLADataEntryRepository slaDataEntryRepository;

    @Autowired
    private SLADataTypeRepository slaDataTypeRepository;

    @Autowired
    private SLADataTypeService slaDataTypeService;

    public SLADataEntryDto save(SLADataEntry slaDataEntry) {

        SLADataType slaDataType = slaDataTypeRepository.findById(slaDataEntry.getDataTypeId()).orElse(null);
        if (slaDataType == null) {
            return null;
        }

        SLADataEntry slaDataEntryBuild = SLADataEntry.builder().value(slaDataEntry.getValue())
                .businessStartDate(slaDataEntry.getBusinessStartDate())
                .businessEndDate(slaDataEntry.getBusinessEndDate()).createDate(slaDataEntry.getCreateDate())
                .comments(slaDataEntry.getComments()).dataTypeId(slaDataType.getId()).build();

        SLADataEntry slaDataEntryNew = slaDataEntryRepository.save(slaDataEntryBuild);

        return mapDto(slaDataEntryNew);
    }

    public List<SLADataEntryDto> getAll() {
        List<SLADataEntry> dataEntries = slaDataEntryRepository.findAll();

        return dataEntries.stream().map(SLADataEntry -> mapDto(SLADataEntry)).collect(Collectors.toList());
    }

    public SLADataEntryDto getById(long id) {
        SLADataEntry slaDataEntry = slaDataEntryRepository.findById(id).orElse(null);
        if (slaDataEntry != null) {
            return mapDto(slaDataEntry);
        }
        return null;
    }

    public SLADataEntryDto update(long id, SLADataEntry slaDataEntry) {
        SLADataEntry slaDataEntryUpdated = slaDataEntryRepository.findById(id).orElse(null);
        if (slaDataEntryUpdated == null) {
            return null;
        }

        SLADataType SLADataType = slaDataTypeRepository.findById(slaDataEntry.getDataTypeId()).orElse(null);
        if (SLADataType == null) {
            return null;
        }

        slaDataEntryUpdated.setValue(slaDataEntry.getValue());
        slaDataEntryUpdated.setBusinessStartDate(slaDataEntry.getBusinessStartDate());
        slaDataEntryUpdated.setBusinessEndDate(slaDataEntry.getBusinessEndDate());
        slaDataEntryUpdated.setCreateDate(slaDataEntry.getCreateDate());
        slaDataEntryUpdated.setComments(slaDataEntry.getComments());
        slaDataEntryUpdated.setDataTypeId(slaDataEntry.getDataTypeId());

        slaDataEntryRepository.save(slaDataEntryUpdated);

        return mapDto(slaDataEntryUpdated);
    }

    public boolean delete(long id) {
        SLADataEntry SLADataEntry = slaDataEntryRepository.findById(id).orElse(null);
        if (SLADataEntry != null) {
            slaDataEntryRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public SLADataEntryDto mapDto(SLADataEntry slaDataEntry) {

        SLADataType SLADataType = slaDataTypeRepository.findById(slaDataEntry.getDataTypeId()).orElse(null);
        if (SLADataType == null) {
            return null;
        }

        SLADataTypeDto SLADataTypeDto = slaDataTypeService.mapDto(SLADataType);

        SLADataEntryDto dto = new SLADataEntryDto();
        dto.setId(slaDataEntry.getId());
        dto.setValue(slaDataEntry.getValue());
        dto.setBusinessStartDate(slaDataEntry.getBusinessStartDate());
        dto.setBusinessEndDate(slaDataEntry.getBusinessEndDate());
        dto.setCreateDate(slaDataEntry.getCreateDate());
        dto.setComments(slaDataEntry.getComments());
        dto.setDataTypeId(SLADataTypeDto);

        return dto;
    }
}
