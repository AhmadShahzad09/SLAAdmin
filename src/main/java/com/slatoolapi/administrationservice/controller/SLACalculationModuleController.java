package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLACalculationModuleDto;
import com.slatoolapi.administrationservice.entity.SLACalculationModule;
import com.slatoolapi.administrationservice.service.SLACalculationModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLACalculationModuleController {

    @Autowired
    private SLACalculationModuleService slaCalculationModuleService;

    @GetMapping("/calculation-module")
    public ResponseEntity<List<SLACalculationModuleDto>> getAll() {

        return new ResponseEntity<List<SLACalculationModuleDto>>(slaCalculationModuleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/calculation-module/{id}")
    public ResponseEntity<SLACalculationModuleDto> getById(@PathVariable("id") long id) {
        SLACalculationModuleDto dto = slaCalculationModuleService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLACalculationModuleDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/calculation-module")
    public ResponseEntity save(@RequestBody SLACalculationModule slaCalculationModule) {
        SLACalculationModuleDto dto = slaCalculationModuleService.save(slaCalculationModule);

        if (dto == null) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SLACalculationModuleDto>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/calculation-module/{id}")
    public ResponseEntity<SLACalculationModuleDto> update(@PathVariable("id") long id,
            @RequestBody SLACalculationModule slaCalculationModule) {
        SLACalculationModuleDto dto = slaCalculationModuleService.update(id, slaCalculationModule);

        if (dto != null) {
            return new ResponseEntity<SLACalculationModuleDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/calculation-module/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaCalculationModuleService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

}
