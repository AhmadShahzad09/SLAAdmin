package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLACalculationMethodObjectiveDto;
import com.slatoolapi.administrationservice.entity.SLACalculationMethodsObjective;
import com.slatoolapi.administrationservice.service.SLACalculationMethodsObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLACalculationMethodsObjectiveController {

    @Autowired
    private SLACalculationMethodsObjectiveService slaCalculationMethodsObjectiveService;

    @GetMapping("/calculation-objective")
    public ResponseEntity<List<SLACalculationMethodObjectiveDto>> getAll() {

        return new ResponseEntity<List<SLACalculationMethodObjectiveDto>>(
                slaCalculationMethodsObjectiveService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/calculation-objective/{id}")
    public ResponseEntity<SLACalculationMethodObjectiveDto> getById(@PathVariable("id") long id) {
        SLACalculationMethodObjectiveDto dto = slaCalculationMethodsObjectiveService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLACalculationMethodObjectiveDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/calculation-objective")
    public ResponseEntity save(@RequestBody SLACalculationMethodsObjective slaCalculationMethodsObjective) {
        SLACalculationMethodObjectiveDto dto = slaCalculationMethodsObjectiveService
                .save(slaCalculationMethodsObjective);

        if (dto == null) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SLACalculationMethodObjectiveDto>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/calculation-objective/{id}")
    public ResponseEntity<SLACalculationMethodObjectiveDto> update(@PathVariable("id") long id,
            @RequestBody SLACalculationMethodsObjective slaCalculationMethodsObjective) {
        SLACalculationMethodObjectiveDto dto = slaCalculationMethodsObjectiveService.update(id,
                slaCalculationMethodsObjective);

        if (dto != null) {
            return new ResponseEntity<SLACalculationMethodObjectiveDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/calculation-objective/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaCalculationMethodsObjectiveService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }
}
