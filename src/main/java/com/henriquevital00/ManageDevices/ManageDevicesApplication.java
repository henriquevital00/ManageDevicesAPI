package com.henriquevital00.ManageDevices;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
		info = @Info(
				title = "Manage Devices REST API Documentation",
				description = "Manage Devices REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Henrique Vital Carvalho",
						email = "henriquevital00@gmail.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Manage Devices REST API Documentation"
		)
)
public class ManageDevicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageDevicesApplication.class, args);
	}

}
