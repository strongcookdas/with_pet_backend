package com.ajou_nice.with_pet.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

	// 기본적으로 spring boot에서의 scheduling은 single thread pool에서 실행되므로
	// 매일 새벽 3시에 2개의 scheduling작업을 동시에 수행할 수 있게 하기 위해서 thread pool을 설정한다.
	private final int POOL_SIZE = 10;

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar){
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
		threadPoolTaskScheduler.setThreadNamePrefix("reservation-auto-task");
		threadPoolTaskScheduler.initialize();

		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

	}
}
