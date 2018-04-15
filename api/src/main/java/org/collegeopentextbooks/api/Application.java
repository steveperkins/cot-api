package org.collegeopentextbooks.api;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.collegeopentextbooks.api.interceptor.RequestLoggerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

@EnableAutoConfiguration
@ComponentScan("org.collegeopentextbooks.api")
public class Application extends WebMvcConfigurerAdapter {
	public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return new BasicDataSource();
	}
	
	@Bean
	public MappedInterceptor myInterceptor()
	{
	    return new MappedInterceptor(null, new RequestLoggerInterceptor());
	}
}

