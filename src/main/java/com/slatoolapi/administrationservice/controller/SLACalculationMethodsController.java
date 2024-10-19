package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.*;
import com.slatoolapi.administrationservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLACalculationMethodsController {

    @Autowired
    private SLACalculationMethodsService slaCalculationMethodsService;

    @GetMapping("/calculation-methods")
    public ResponseEntity<List<SLACalculationMethodsDto>> getAll() {

        return new ResponseEntity<List<SLACalculationMethodsDto>>(slaCalculationMethodsService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/calculation-methods/{id}")
    public ResponseEntity<SLACalculationMethodsDto> getById(@PathVariable("id") long id) {
        SLACalculationMethodsDto dto = slaCalculationMethodsService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLACalculationMethodsDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/calculation-methods")
    public ResponseEntity save(@RequestBody SLACalculationMethodsPostDto slaCalculationMethodsPostDto) {
        SLACalculationMethodsDto dto = slaCalculationMethodsService.save(slaCalculationMethodsPostDto);
        if (dto == null) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<SLACalculationMethodsDto>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/calculation-methods/{id}")
    public ResponseEntity<SLACalculationMethodsDto> update(@PathVariable("id") long id,
            @RequestBody SLACalculationMethodsPostDto slaCalculationMethodsPostDto) {
        SLACalculationMethodsDto dto = slaCalculationMethodsService.update(id, slaCalculationMethodsPostDto);

        if (dto != null) {
            return new ResponseEntity<SLACalculationMethodsDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/calculation-methods/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaCalculationMethodsService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

}
