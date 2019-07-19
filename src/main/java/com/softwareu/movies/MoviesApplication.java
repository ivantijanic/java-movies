package com.softwareu.movies;


import com.softwareu.movies.helper.InitData;
import com.softwareu.movies.storage.StorageProperties;
import com.softwareu.movies.service.StorageService;
import com.softwareu.movies.controller.aspect.CounterCalls;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(StorageProperties.class)
public class MoviesApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

	@Autowired
	private InitData initData;

	@Autowired
	private CounterCalls counterCalls;

	@Override
	public void run(String... args) throws Exception {
		Counter counter = Metrics.counter("rest.calls");
		counter.increment(counterCalls.previousCount());

		initData.init();
	}

}
