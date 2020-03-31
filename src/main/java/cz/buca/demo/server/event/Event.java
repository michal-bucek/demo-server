package cz.buca.demo.server.event;

import lombok.Data;

@Data
public class Event {

	private String sessionId;
	private String userName;
	private String type;
	private Object data;
}