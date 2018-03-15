package org.flysky.coder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class CoderApplication extends SpringBootServletInitializer {

	// to run in outer tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoderApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
