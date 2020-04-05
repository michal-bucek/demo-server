package cz.buca.demo.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Autowired
	private ApplicationConfig applicationConfig;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		String origin = applicationConfig.getCors().getOrigin();
		
		registry.addEndpoint("/socket").setAllowedOrigins(origin);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/event/user");
		registry.setApplicationDestinationPrefixes("/app");
	}
}