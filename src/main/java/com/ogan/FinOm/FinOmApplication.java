package com.ogan.FinOm;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The FinOm Banking App",
				description = "Backend Rest APIs for FinOm Bank",
				version = "v1.0",
				contact = @Contact(
						name = "Omkar Ganjale",
						email = "omkarganjale1310@gmail.com",
						url = "https://github.com/Omkarganjale"
				),
				license = @License(
						name = "The FinOm Bank",
						url = "https://github.com/Omkarganjale/FinOm"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The FinOm Bank App Documentation",
				url = "https://github.com/Omkarganjale/FinOm"
		)
)
public class FinOmApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinOmApplication.class, args);
	}

}
