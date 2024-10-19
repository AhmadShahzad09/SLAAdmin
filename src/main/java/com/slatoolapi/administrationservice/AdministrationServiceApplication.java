package com.slatoolapi.administrationservice;

import java.util.concurrent.Executor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@SpringBootApplication
@EnableEurekaClient
public class AdministrationServiceApplication {

	@Value("${NUMBER_THREADS}")
    private int numberThreads;
	//@Value("${NUMBER_THREADS:6}")
    //private int numberThreads;

	//int numberThreads=6;

	public static void main(String[] args) {
		SpringApplication.run(AdministrationServiceApplication.class, args);
	}



	@Bean(name = "useAsyncA")
	public Executor exeAsyncA() {
		ThreadPoolTaskExecutor executorA = new ThreadPoolTaskExecutor();
		executorA.setCorePoolSize(numberThreads); 
		executorA.setMaxPoolSize(numberThreads); 
		executorA.setQueueCapacity(6400); 
		executorA.setThreadNamePrefix("useAA-");
		executorA.initialize();
		return executorA;
	}

	
	

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
			}
		};
	}
}
