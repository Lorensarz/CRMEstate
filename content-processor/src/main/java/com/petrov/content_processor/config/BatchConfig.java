package com.petrov.content_processor.config;
import com.petrov.content_processor.batch.EstateItemProcessor;
import com.petrov.content_processor.batch.EstateItemWriter;
import com.petrov.content_processor.batch.EstateProcessingException;
import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	@Bean
	public Job estateProcessingJob(JobRepository jobRepository,
								   Step estateProcessingStep,
								   JobExecutionListener jobExecutionListener) {
		return new JobBuilder("estateProcessingJob", jobRepository)
				.listener(jobExecutionListener)
				.start(estateProcessingStep)
				.build();
	}

	@Bean
	public Step estateProcessingStep(JobRepository jobRepository,
									 PlatformTransactionManager transactionManager,
									 ItemReader<EstateDataDto> estateItemReader,
									 EstateItemProcessor processor,
									 EstateItemWriter writer) {
		return new StepBuilder("estateProcessingStep", jobRepository)
				.<EstateDataDto, Estate>chunk(100, transactionManager)
				.reader(estateItemReader)
				.processor(processor)
				.writer(writer)
				.faultTolerant()
				.skipLimit(10)
				.skip(EstateProcessingException.class)
				.build();
	}
}