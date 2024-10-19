package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLADataTypeDto;
import com.slatoolapi.administrationservice.entity.SLADataType;
import com.slatoolapi.administrationservice.service.SLADataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLADataTypeController {

    @Autowired
    private SLADataTypeService slaDataTypeService;

    @GetMapping("/data-type")
    public ResponseEntity<List<SLADataTypeDto>> getAll() {

        return new ResponseEntity<List<SLADataTypeDto>>(slaDataTypeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/data-type/{id}")
    public ResponseEntity<SLADataTypeDto> getById(@PathVariable("id") long id) {
        SLADataTypeDto dto = slaDataTypeService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLADataTypeDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/data-type")
    public ResponseEntity<SLADataTypeDto> save(@RequestBody SLADataType slaDataType) {

        return new ResponseEntity<SLADataTypeDto>(slaDataTypeService.save(slaDataType), HttpStatus.CREATED);
    }

    @PutMapping("/data-type/{id}")
    public ResponseEntity<SLADataTypeDto> update(@PathVariable("id") long id, @RequestBody SLADataType slaDataType) {
        SLADataTypeDto dto = slaDataTypeService.update(id, slaDataType);

        if (dto != null) {
            return new ResponseEntity<SLADataTypeDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/data-type/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaDataTypeService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

}
