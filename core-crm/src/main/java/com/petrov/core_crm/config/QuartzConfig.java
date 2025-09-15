package com.petrov.core_crm.config;

import com.petrov.core_crm.job.InactiveBuildingsJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

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