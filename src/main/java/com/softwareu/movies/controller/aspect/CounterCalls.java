package com.softwareu.movies.controller.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Aspect
public class CounterCalls {

    @Value("${counter.file.path}")
    private String COUNTER_FILE_PATH;

    private static final Logger LOG = LoggerFactory.getLogger(CounterCalls.class);

    private Counter counterAllMovies = Metrics.counter("rest.calls");

    @Before("execution(* com.softwareu.movies.controller.*.*(..))")
    public double getCountOfCalss()throws IOException{
        counterAllMovies.increment();
        Path path = Paths.get(COUNTER_FILE_PATH);
        String fileContent = counterAllMovies.count() + "";
        LOG.info(fileContent + " PRINTED");
        if(Files.notExists(path)) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, fileContent.getBytes());
        return counterAllMovies.count();
    }

    public double previousCount() throws IOException {
        File file = ResourceUtils.getFile(COUNTER_FILE_PATH);
        if (file.exists()) {
            String content = new String(Files.readAllBytes(file.toPath()));
            double retValue = Double.parseDouble(content.toString());
            return retValue;
        }
        return 0;
    }


}
