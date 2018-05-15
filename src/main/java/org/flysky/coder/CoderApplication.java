package org.flysky.coder;

import org.flysky.coder.filter.CustomSessionMangerFilter;
import org.flysky.coder.listener.CustomSessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class CoderApplication extends SpringBootServletInitializer {

	// to run in outer tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoderApplication.class);
	}

	@Bean
	public ServletListenerRegistrationBean customSessionListenerRegistration() {
		ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(new CustomSessionListener());
		return servletListenerRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean indexFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(new CustomSessionMangerFilter());
		registration.addUrlPatterns("/*");
		return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
