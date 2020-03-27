package cz.buca.demo.server.dto;

import java.util.List;

import lombok.Data;

@Data
public class Session {

	private String name;
	private String token;
	private String refresh;
	private List<String> roles;
	
}