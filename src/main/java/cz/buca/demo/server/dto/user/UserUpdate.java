package cz.buca.demo.server.dto.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdate {

	private String name;
	private String login;
	private String email;
	private List<String> roles;
}