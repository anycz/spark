package com.ccc.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Classe de configuration mère. Point d'entrée de Spring Boot
 *
 * @author nycz_a
 */
@Configuration
@EnableAutoConfiguration
@EnableAsync
@ComponentScan(value = {"com.ccc.*"})
public class SparkConfig extends SpringBootServletInitializer {

    /**
     * Démarrage de Spring Boot sur un Tomcat Embedded
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        new SpringApplication(SparkConfig.class).run(args);
    }
}
