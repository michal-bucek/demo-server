package cz.buca.demo.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cz.buca.demo.server.entity.User;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {
	
	private Token token;
	private Cors cors;	
	private User admin;
	
	@Data
	public static class Token {
		
		private String secret;
		private Integer expiration;
		private Integer refresh;
	}

	@Data
	public static class Cors {
		
		private String origin; 
	}
}