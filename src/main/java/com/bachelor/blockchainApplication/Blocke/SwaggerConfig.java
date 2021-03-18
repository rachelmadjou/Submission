package com.bachelor.blockchainApplication.Blocke;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
	@Bean
	public Docket blockchainApi() {
		
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.bachelor.blockchainApplication"))
				.build();
			//	.apiInfo(informationApi());
								
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Blockchain API")
				.description("this API can be used to track all changed that mades into a Dataset and store"
						+ " it into the blockchain to garanted the Integrity")
				.version("1.0")
				.build();
				
	}
	
}
