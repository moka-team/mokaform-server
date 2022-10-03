package com.mokaform.mokaformserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MokaformServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MokaformServerApplication.class, args);
    }

}
