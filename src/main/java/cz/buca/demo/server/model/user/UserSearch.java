package cz.buca.demo.server.model.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearch {

	private Long id;
	private String name;
	private String login;
	private String email;
	private Boolean active;
	
	private String modifier;
	private Date modified;
}