package org.flysky.coder;

import org.flysky.coder.filter.CustomSessionMangerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableCaching
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class CoderApplication extends SpringBootServletInitializer {


	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

	// to run in outer tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoderApplication.class);
	}

	@Bean
	public FilterRegistrationBean indexFilterRegistration() {
		CustomSessionMangerFilter filter = new CustomSessionMangerFilter();
		FilterRegistrationBean registration = new FilterRegistrationBean();
		filter.setSessionRepository(sessionRepository);
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
