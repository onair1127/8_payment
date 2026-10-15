package com.anseong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.anseong.global.hints.ApplicationHints;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ImportRuntimeHints(ApplicationHints.class)
public class AnseongAppApplication {

     public static void main(String[] args) {
          SpringApplication.run(AnseongAppApplication.class, args);
	}
}
