package com.slatoolapi.administrationservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.slatoolapi.administrationservice.entity.SLACalculationMethods;
import com.slatoolapi.administrationservice.entity.SLAExecution;
import com.slatoolapi.administrationservice.entity.SLASchedule;
import com.slatoolapi.administrationservice.repository.SLACalculationMethodsRepository;
import com.slatoolapi.administrationservice.repository.SLAExecutionRepository;
import com.slatoolapi.administrationservice.repository.SLAScheduleRepository;
import com.slatoolapi.administrationservice.service.CalculationResultService;
import com.slatoolapi.administrationservice.service.SLAAsyncExecutionService;
import com.slatoolapi.administrationservice.service.SLAExecutionService;

import java.util.List;

@SpringBootTest
@ActiveProfiles("prod") 
@Transactional
@Rollback(value=false)
public class CrearPlanificacionMasiva {
	 private static final Logger LOGGER = LoggerFactory.getLogger(CrearPlanificacionMasiva.class);
    @Autowired
    SLAExecutionService executionService;
    
    @Autowired
    SLAAsyncExecutionService asyncExe;
    
    @Autowired
    SLAExecutionRepository exRepo;
    
    @Autowired
    CalculationResultService exCal;
    
    @Autowired
    SLACalculationMethodsRepository calRepo;
    
    @Autowired
    SLAScheduleRepository scheRepo;
    
    
	@Test
	void createExecution() {
		
//		SLAExecution slaExecutionBuild = SLAExecution.builder().scheduleId(slaSchedule.getId())
//	             .slaName(slaCalculationMethodsNew.getName()).status("pending").slaDate(now.plus(period))
//	             .startDateTime(dtExtract).endDateTime(dtExtract).executionResult("").audit(false).build();
//		
		
//		SLAExecution slaExecutionBuild = SLAExecution.
	//	executionService.create(26);
//		executionService.create(14);
		//26
		//14
		//109842
		//SLACalculationMethods sla=calRepo.slaCalculationMethods("SL#03");
		//SLACalculationMethods sla=calRepo.slaCalculationMethods("SL#05");
				//executionService.create(sla.getId());
		
		
		//SLAExecution exec=exRepo.getById(95393L);
	 	 
	 	String slaName = "SL#16";
        LocalDate startDate = LocalDate.parse("2024-03-24");
        LocalDate endDate = LocalDate.parse("2024-04-02");
        
//        List<SLAExecution>  slaExecutions = exRepo.findBySlaNameAndSlaDateBetween(slaName, startDate, endDate);
        
        SLAExecution slaExecutionBuild = SLAExecution.builder()
                .scheduleId(slaSchedule.getId())
                .slaName(slaName)
                .type(slaCalculationMethods.getSlaType())
                .description(slaCalculationMethods.getDescription())
                .status("pending")
                .slaDate(convertDateFinal).startDateTime(dtExtract)
                .endDateTime(dtExtract).zone(zone)
                .executionResult(""). // before "Not ok"
                audit(false). // slaExecutionPostDto.getIsAudit()
                build();
        
        long cal = 96015;
        asyncExe.forAudit(slaExecutionBuild, cal);
		
		assert(true);
	}
	 
}
