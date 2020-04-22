package cz.buca.demo.server.model;

import lombok.Data;

@Data
public class Event {

	private String sessionUuid;
	private String userName;
	private String type;
	private Object data;
}