package org.collegeopentextbooks.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.collegeopentextbooks.api.importer.CotHtmlImporter;
import org.collegeopentextbooks.api.importer.ExampleImporter;
import org.collegeopentextbooks.api.importer.FloridaVirtualCampusImporter;
import org.collegeopentextbooks.api.importer.Importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Console application for running overnight imports
 * @author steve.perkins
 *
 */
@EnableAutoConfiguration
@ComponentScan("org.collegeopentextbooks.api")
@PropertySource("classpath:application.properties")
public class ParserApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(ParserApplication.class);
        try {
	        ParserApplication parser = context.getBean(ParserApplication.class);
	        parser.start();
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
	private ExampleImporter exampleImporter;
	
	@Autowired
	private CotHtmlImporter cotHtmlImporter;
	
	@Autowired
	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
    public void start() {
    	cotHtmlImporter.setInputFolder(new File("F:/consultingprojects/collegeopentextbooks/listings_html"));
    	List<Importer> importers = new ArrayList<>();
    	importers.add(cotHtmlImporter);
//    	importers.add(floridaVirtualCampusImporter);
//    	importers.add(exampleImporter);
    	
    	for(Importer importer: importers) {
    		importer.run();
    	}
    }
}
