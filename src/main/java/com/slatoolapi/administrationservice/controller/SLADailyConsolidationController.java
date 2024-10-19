package com.slatoolapi.administrationservice.controller;

import com.slatoolapi.administrationservice.entity.SLADailyConsolidation;
import com.slatoolapi.administrationservice.service.SLADailyConsolidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/administration")
public class SLADailyConsolidationController {
    @Autowired
    private SLADailyConsolidationService slaDailyConsolidationService;

    @GetMapping(value = "/calculation-result-monthly", params = { "last" })
    public List<SLADailyConsolidation> getAll(@RequestParam boolean last) {
        return slaDailyConsolidationService.autoCallMonthly();
    }

    @GetMapping("/executeResultMonthly")
    public Map<String, String> executeResultMonthly(@RequestParam Optional<Integer> year,
            @RequestParam Optional<Integer> month) {
        slaDailyConsolidationService.resultMonthly(year, month);
        Map<String, String> jsonHardcodeado = new HashMap<>();
        jsonHardcodeado.put("Out", "executed correctly");
        jsonHardcodeado.put("year", year.toString());
        jsonHardcodeado.put("month", month.toString());

        return jsonHardcodeado;

    }

}
