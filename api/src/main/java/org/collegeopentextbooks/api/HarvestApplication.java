package org.collegeopentextbooks.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.collegeopentextbooks.api.importer.CotHtmlImporter;
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
import org.springframework.stereotype.Component;

//@EnableAutoConfiguration
//@ComponentScan("org.collegeopentextbooks.api")
//@PropertySource("classpath:application.properties")
//@Component
public class HarvestApplication {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(HarvestApplication.class);
        try {
	        HarvestApplication harvester = context.getBean(HarvestApplication.class);
	        for(String arg: args) {
	        	if(null != arg && arg.startsWith("inputFolder=")) {
	        		harvester.setInputFolder(arg.split("=")[1]);
	        	}
	        }
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
	
    
//	@Autowired
//	private ExampleImporter exampleImporter;
//	
//	@Autowired
//	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
	@Autowired
	private CotHtmlImporter cotHtmlImporter;
	
	private String inputFolder;
	
    public void start() {
    	cotHtmlImporter.setInputFolder(new File(inputFolder));
    	List<Importer> importers = new ArrayList<>();
    	importers.add(cotHtmlImporter);
//    	importers.add(floridaVirtualCampusImporter);
//    	importers.add(exampleImporter);
    	
    	for(Importer importer: importers) {
    		importer.run();
    	}
    }
    
    public void setInputFolder(String inputFolder) {
    	this.inputFolder = inputFolder;
    }
}
