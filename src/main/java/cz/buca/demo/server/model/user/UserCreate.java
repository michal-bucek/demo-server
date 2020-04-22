package cz.buca.demo.server.model.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreate {

	private String name;
	private String login;
	private String pass;
	private String email;
	private Boolean active;
	private List<String> roles;
}
