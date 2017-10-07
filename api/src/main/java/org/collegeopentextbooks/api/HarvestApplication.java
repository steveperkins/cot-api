package org.collegeopentextbooks.api;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.collegeopentextbooks.api.importer.FloridaVirtualCampusImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@ComponentScan("org.collegeopentextbooks.api")
@PropertySource("classpath:application.properties")
public class HarvestApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(HarvestApplication.class);
        try {
	        HarvestApplication harvester = context.getBean(HarvestApplication.class);
	        harvester.start();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
    }

	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	
	@Value("${spring.datasource.username}")
	private String datasourceUsername;
	
	@Value("${spring.datasource.password}")
	private String datasourcePassword;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl(datasourceUrl);
		datasource.setUsername(datasourceUsername);
		datasource.setPassword(datasourcePassword);
		return datasource;
	}
	
    
	@Autowired
	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
    public void start() {
    	floridaVirtualCampusImporter.run();
    }
}
