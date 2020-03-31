package cz.buca.demo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.event.Event;
import cz.buca.demo.server.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public void publish(String type, Object data) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Event event = new Event();
		
		event.setSessionId(userPrincipal.getId());
		event.setUserName(userPrincipal.getName());
		event.setType(type);
		event.setData(data);
		
		log.debug("publish "+ event);
		
		messagingTemplate.convertAndSend("/event", event);
		eventPublisher.publishEvent(event);
	}
}