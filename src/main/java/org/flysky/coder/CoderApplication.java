package org.flysky.coder;

import org.flysky.coder.filter.CustomSessionMangerFilter;
import org.flysky.coder.filter.EncodingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.Charset;
import java.util.List;

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
		registration.setOrder(1);
		return registration;
	}

	//@Bean
	public FilterRegistrationBean encodingFilterRegistration() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8",true);
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setOrder(2);
		return registration;
	}

	//@Bean
	public HttpMessageConverter<String> mvcConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		return converter;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
