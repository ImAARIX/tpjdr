package com.lescours.tpjdrspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.lescours.tpjdrspringapi.repository")
@EntityScan("com.lescours.tpjdrspringapi.model")
public class TpjdrSpringApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpjdrSpringApiApplication.class, args);
    }

}
