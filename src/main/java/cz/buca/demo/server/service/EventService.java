package cz.buca.demo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.model.Event;
import cz.buca.demo.server.security.UserSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public void publish(String destination, String type, Object data) {
		Event event = new Event();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			UserSession userSession = (UserSession) authentication.getPrincipal();
			
			event.setSessionUuid(userSession.getUuid());
			event.setUserName(userSession.getName());	
		}
		
		event.setType(type);
		event.setData(data);
		
		log.debug("publish "+ event);
		
		messagingTemplate.convertAndSend(destination, event);
		eventPublisher.publishEvent(event);
	}
}