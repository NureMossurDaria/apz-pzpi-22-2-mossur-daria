package ua.nure.mossurd.blooddosyst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@ComponentScan(basePackages = "ua.nure.mossurd.blooddosyst")
public class BlooddosystApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlooddosystApplication.class, args);
	}

}
