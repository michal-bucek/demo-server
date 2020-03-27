package cz.buca.demo.server.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {

	private Long id;
	private String name;
	private String login;
	private String email;
	private List<String> roles;
	
	private String creator;
	private Date created;
	private String modifier;
	private Date modified;
}