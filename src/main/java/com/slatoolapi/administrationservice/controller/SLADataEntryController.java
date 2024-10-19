package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLADataEntryDto;
import com.slatoolapi.administrationservice.entity.SLADataEntry;
import com.slatoolapi.administrationservice.service.SLADataEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLADataEntryController {

    @Autowired
    private SLADataEntryService slaDataEntryService;

    @GetMapping("/data-entry")
    public ResponseEntity<List<SLADataEntryDto>> getAll() {

        return new ResponseEntity<List<SLADataEntryDto>>(slaDataEntryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/data-entry/{id}")
    public ResponseEntity<SLADataEntryDto> getById(@PathVariable("id") long id) {
        SLADataEntryDto dto = slaDataEntryService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLADataEntryDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/data-entry")
    public ResponseEntity save(@RequestBody SLADataEntry slaDataEntry) {
        SLADataEntryDto dto = slaDataEntryService.save(slaDataEntry);

        if (dto == null) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SLADataEntryDto>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/data-entry/{id}")
    public ResponseEntity<SLADataEntryDto> update(@PathVariable("id") long id, @RequestBody SLADataEntry slaDataEntry) {
        SLADataEntryDto dto = slaDataEntryService.update(id, slaDataEntry);

        if (dto != null) {
            return new ResponseEntity<SLADataEntryDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/data-entry/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaDataEntryService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

}
