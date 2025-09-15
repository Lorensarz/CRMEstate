package com.petrov.content_processor.config;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchProcessingConfig {

	@Bean
	public JobExecutionListener jobExecutionListener() {
		return new org.springframework.batch.core.JobExecutionListener() {
			@Override
			public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
				System.out.println("Job started: " + jobExecution.getJobInstance().getJobName());
			}

			@Override
			public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
				System.out.println("Job completed with status: " + jobExecution.getStatus());
			}
		};
	}
}