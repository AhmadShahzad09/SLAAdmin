package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.SLAExecutionDto;
import com.slatoolapi.administrationservice.dto.SLAExecutionPostDto;
import com.slatoolapi.administrationservice.entity.SLAExecution;
import com.slatoolapi.administrationservice.repository.SLAExecutionRepository;
import com.slatoolapi.administrationservice.service.SLAExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.File;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
@Slf4j
public class SLAExecutionController {

    @Autowired
    private SLAExecutionService slaExecutionService;

    @Autowired
    private SLAExecutionRepository slaExecutionRepository;
    
    @GetMapping("/execution")
    public Page<SLAExecutionDto> getAll(
            @RequestParam(required = false, defaultValue = "") String sla,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String result,
            @RequestParam(required = false, defaultValue = "") String date,
            @RequestParam(required = false, defaultValue = "") String userId,
            @RequestParam(required = false, defaultValue = "") String startdate,
            @RequestParam(required = false, defaultValue = "") String enddate,
            @RequestParam(required = false, defaultValue = "") String lastmonth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false, defaultValue = "false") String audit,
            Pageable pageable) throws Exception {
        Boolean bAudit = Boolean.parseBoolean(audit);
        try {

            if (size < 0 && lastmonth.equals("1")) {
                Pageable paging = PageRequest.of(page, Integer.MAX_VALUE);

                return slaExecutionService.getLastMonthly(paging, bAudit);
            }

            if (size < 0) {
                Pageable paging = PageRequest.of(page, Integer.MAX_VALUE);
                return slaExecutionService.getAllExecution(paging, bAudit);
            }

            if (size > 0) {
                Pageable paging = PageRequest.of(page, size);
                if (page > 0) {
                    paging = PageRequest.of(page - 1, size);
                }

                if (lastmonth.equals("1")) {
                    log.info("show data only lastmonth -> {}", userId);
                    return slaExecutionService.getAllfilterTestMonthly(sla, status, result, date, startdate, enddate,
                            userId, paging, bAudit);
                } else {
                    log.info("show data without lastmonth -> ", userId);
                    return slaExecutionService.getAllFiltersTest(sla, status, result, date, startdate, enddate, userId,
                            paging, bAudit);
                }
            } else {
                return null;
            }

        } catch (Exception ex) {
            log.error("message exception -> {}", ex.getMessage());
            log.error("message localized -> {}", ex.getLocalizedMessage());
            log.error("message cause -> {}", ex.getCause());
            throw new Exception("error in page for sla execution - dto");
        }
    }

    @GetMapping("/execution/{id}")
    public ResponseEntity<SLAExecutionDto> getById(@PathVariable("id") long id) {
        SLAExecutionDto dto = slaExecutionService.getById(id);

        if (dto != null) {
            return new ResponseEntity<SLAExecutionDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/execution")
    public ResponseEntity save(@RequestBody SLAExecutionPostDto slaExecutionPostDto) {
        try {
            if (slaExecutionPostDto.getTime() == null) {
                LocalTime horaActual = LocalTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
                String horaFormateada = horaActual.format(formatter);
                slaExecutionPostDto.setTime(horaFormateada);
            }

            if (slaExecutionPostDto.getIsAudit() == null) {
                slaExecutionPostDto.setIsAudit(false);
            }

            if (slaExecutionPostDto.getIsAudit()) {
                LocalTime horaActual = LocalTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
                String horaFormateada = horaActual.format(formatter);
                slaExecutionPostDto.setTime(horaFormateada);
            }

            SLAExecutionDto dto = slaExecutionService.save(slaExecutionPostDto);
           

            if (dto == null) {
                return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<SLAExecutionDto>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
           
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/execution/{id}")
    public ResponseEntity<SLAExecutionDto> update(@PathVariable("id") long id, @RequestBody SLAExecution slaExecution) {
        SLAExecutionDto dto = slaExecutionService.update(id, slaExecution);

        if (dto != null) {
            return new ResponseEntity<SLAExecutionDto>(dto, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/execution/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Boolean isDeleted = slaExecutionService.delete(id);

        if (isDeleted) {
            return new ResponseEntity<String>("True", HttpStatus.OK);
        }

        return new ResponseEntity<String>("False", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/audit/{id}")
    public ResponseEntity<Resource> downloadCSV(@PathVariable Long id) {
        SLAExecution execution = slaExecutionRepository.findById(id).orElse(null);
        if (execution == null || execution.getUrl() == null) {
            return ResponseEntity.notFound().build();
        }
        try {

            File file = new File(execution.getUrl());
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            FileSystemResource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(resource);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
