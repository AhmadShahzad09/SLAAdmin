package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLAScheduleDto;
import com.slatoolapi.administrationservice.entity.SLASchedule;
import com.slatoolapi.administrationservice.service.SLAScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLAScheduleController {

    @Autowired
    private SLAScheduleService slaScheduleService;

    @GetMapping("/schedule")
    public ResponseEntity<List<SLAScheduleDto>> getAll() {

        return new ResponseEntity<List<SLAScheduleDto>>(slaScheduleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<SLAScheduleDto> getById(@PathVariable("id") long id) {
        SLAScheduleDto dto = slaScheduleService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLAScheduleDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/schedule")
    public ResponseEntity save(@RequestBody SLASchedule slaSchedule) {
        SLAScheduleDto dto = slaScheduleService.save(slaSchedule);

        if (dto == null) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SLAScheduleDto>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<SLAScheduleDto> update(@PathVariable("id") long id, @RequestBody SLASchedule slaSchedule) {
        SLAScheduleDto dto = slaScheduleService.update(id, slaSchedule);

        if (slaSchedule != null) {
            return new ResponseEntity<SLAScheduleDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaScheduleService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

}
