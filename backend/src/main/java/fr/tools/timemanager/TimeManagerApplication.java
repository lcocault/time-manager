package fr.tools.timemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "fr.tools")
@EnableJpaRepositories(basePackages = "fr.tools")
@EntityScan(basePackages = "fr.tools")
public class TimeManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeManagerApplication.class, args);
    }
}
