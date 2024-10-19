package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.dto.CalculationResultDto;
import com.slatoolapi.administrationservice.dto.SLADataMeterDto;
import com.slatoolapi.administrationservice.service.CalculationResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
@Slf4j
public class CalculationResultController {

    @Autowired
    private CalculationResultService calculationResultService;
    
    
    @GetMapping(value = "/calculation-result")
    public Page<CalculationResultDto> getAll(@RequestParam(required = false, defaultValue = "") String last,
            @RequestParam(required = false, defaultValue = "") String sla,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String startdate,
            @RequestParam(required = false, defaultValue = "") String enddate,
            @RequestParam(required = false, defaultValue = "") String zone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String audit,
            Pageable pageable) throws Exception {
        Boolean bAudit;
        if (audit != null) {

            bAudit = Boolean.parseBoolean(audit);
        } else {
            bAudit = null;
        }

        try {

            if (size < 0) {
                log.info("0 -> condition ");
                return calculationResultService.getAllResults(pageable, bAudit);
            }

            if (size > 0) {

                Pageable paging = PageRequest.of(page, size);

                if (page > 0) {
                    paging = PageRequest.of(page - 1, size);
                }

                if (!startdate.isEmpty() && !enddate.isEmpty() && last.contains("1")) {
                    log.info("0.1 -> condition ");
                    return calculationResultService.getAllByFiltersWithLast(paging, zone, sla, type, startdate, enddate,
                            bAudit);
                }

                if (!startdate.isEmpty() && last.contains("1")) {
                    log.info("startdate and last");
                    return calculationResultService.getAllFiltersWithLastStartDate(paging, zone, sla, type, startdate,
                            bAudit);
                }

                if (!enddate.isEmpty() && last.contains("1")) {
                    log.info("end and last");
                    return calculationResultService.getAllFiltersWithLastEndDate(paging, zone, sla, type, enddate,
                            bAudit);
                }

                if (!startdate.isEmpty() && !enddate.isEmpty() && !last.contains("1")) {
                    log.info("1 -> condition ");
                    return calculationResultService.getAllByFiltersWithoutLast(paging, zone, sla, type, startdate,
                            enddate, bAudit);
                }

                if (!startdate.isEmpty() && !last.contains("1")) {
                    log.info("2 -> condition ");
                    return calculationResultService.getAllFiltersWithoutLastStartDate(paging, zone, sla, type,
                            startdate, bAudit);
                }

                if (!enddate.isEmpty() && !last.contains("1")) {
                    log.info("3 -> condition ");
                    return calculationResultService.getAllFiltersWithoutLastEndDate(paging, zone, sla, type, enddate,
                            bAudit);
                }

                if (startdate.isEmpty() && enddate.isEmpty() && !last.contains("1")) {
                    log.info("without date, check");
                    return calculationResultService.getAllByFiltersWithoutDateAndLast(paging, zone, sla, type, bAudit);
                }

                if (startdate.isEmpty() && enddate.isEmpty() && last.contains("1")) {
                    log.info("without date and check ok");
                    return calculationResultService.getAllByFiltersWithLastB(paging, zone, sla, type, startdate,
                            enddate, bAudit);
                }
            } else {
                return null;
            }

        } catch (Exception ex) {
            log.error("message exception -> {}", ex.getMessage());
            log.error("message localized -> {}", ex.getLocalizedMessage());
            log.error("message cause -> {}", ex.getCause());
            throw new Exception("error in page for calculation result - dto");
        }
        return null;
    }
    
    @GetMapping(value = "/calculation-result-report", params = { "last" })
    public Page<CalculationResultDto> getAll(@RequestParam boolean last,
            @RequestParam(required = true, defaultValue = "") String startdate,
            @RequestParam(required = true, defaultValue = "") String enddate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) int size,
            Pageable pageable) {
        Pageable paging = PageRequest.of(page, size);
        return calculationResultService.getAllByFiltersWithoutLast(paging, "", "", "", startdate, enddate, null);
    }
    
    @GetMapping("/calculation-result/zone")
    public ResponseEntity<List<String>> getAllzone() {
        List<String> allZone = calculationResultService.getAllZone();

        return new ResponseEntity<List<String>>(allZone, HttpStatus.OK);
    }
    
    @GetMapping("/calculation-result/failed/{id}")
    public ResponseEntity<List<SLADataMeterDto>> getByIdFailed(@PathVariable("id") long id) {

        List<SLADataMeterDto> dto = calculationResultService.getById(id);
        if (dto != null) {
            return new ResponseEntity<List<SLADataMeterDto>>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
