package com.anseong.global.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AsyncConfiguration implements AsyncConfigurer {

     @Override
     public Executor getAsyncExecutor() {
          return new VirtualThreadTaskExecutor();
     }

     @Bean
     AsyncTaskExecutor virtualTaskExecutor() {
          return new VirtualThreadTaskExecutor();
     }
}
