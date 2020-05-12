package org.camunda.bpm.spring.boot.example.simple;

import java.util.Optional;

import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultJobConfiguration.JobConfiguration;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class JobExecutorCustomizer {

  @Bean(name = JobConfiguration.CAMUNDA_TASK_EXECUTOR_QUALIFIER)
  @ConditionalOnProperty(prefix = "camunda.bpm.job-execution", name = "enabled", havingValue = "true", matchIfMissing = true)
  public static TaskExecutor camundaTaskExecutor(CamundaBpmProperties properties) {
    int corePoolSize = properties.getJobExecution().getCorePoolSize();
    int maxPoolSize = properties.getJobExecution().getMaxPoolSize();
    int queueCapacity = properties.getJobExecution().getQueueCapacity();

    final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

    threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
    threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
    threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
    threadPoolTaskExecutor.setAwaitTerminationSeconds(15);

    Optional.ofNullable(properties.getJobExecution().getKeepAliveSeconds())
      .ifPresent(threadPoolTaskExecutor::setKeepAliveSeconds);

    return threadPoolTaskExecutor;
  }
}
