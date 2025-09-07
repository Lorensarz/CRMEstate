package com.petrov.core_crm.config;

import com.petrov.core_crm.job.InactiveBuildingsJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

	@Bean(name = "coreCrmDataSource")
	@ConfigurationProperties("spring.datasource")
	public DataSource quartzDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public JobDetail checkInactiveBuildingsJobDetail() {
		return JobBuilder.newJob(InactiveBuildingsJob.class)
				.withIdentity("checkInactiveBuildingsJob")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger checkInactiveBuildingsTrigger(JobDetail checkInactiveBuildingsJobDetail) {
		return TriggerBuilder.newTrigger()
				.forJob(checkInactiveBuildingsJobDetail)
				.withIdentity("checkInactiveBuildingsTrigger")
				.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 0))
				.build();
	}

}
