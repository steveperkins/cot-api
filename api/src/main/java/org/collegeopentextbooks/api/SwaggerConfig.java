package org.collegeopentextbooks.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
// TODO Uncomment @Profile if Swagger isn't working. Comment if Spring isn't working. This should get fixed at some point.
@Profile("dev,qa,prod")
@Configuration
@EnableSwagger2
@ComponentScan("org.collegeopentextbooks.api.controller")
public class SwaggerConfig extends WebMvcConfigurationSupport {
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
//           .host("http://localhost:8080")
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
    
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
      // Make Swagger meta-data available via <baseURL>/v2/api-docs/
      registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
      // Make Swagger UI available via <baseURL>/swagger-ui.html
      registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
    }
    
}