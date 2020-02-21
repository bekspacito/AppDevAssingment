package edu.myrza.appdev.labone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class LaboneApplication {

	public static void main(String[] args) {

		SpringApplication.run(LaboneApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfig(){
		System.out.println("cors .... cors");
		return new WebMvcConfigurer(){
			
			public void addCorsMappings(CorsRegistry registry){
			registry.addMapping("/**")
				.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "DELETE")
				.allowedHeaders("*")
                		.allowCredentials(true);
			}

		};
	}

}
