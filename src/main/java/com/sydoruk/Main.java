package com.sydoruk;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan("com.sydoruk.model")
@EnableJpaRepositories("com.sydoruk.repository")
@SpringBootApplication
@EnableScheduling
@Log4j2
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
