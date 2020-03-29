package cz.buca.demo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.event.Event;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public void publish(Event event) {
		log.debug("publish with " + event);
		
		messagingTemplate.convertAndSend("/event", event);
		eventPublisher.publishEvent(event);
	}
}