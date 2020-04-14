package cz.buca.demo.server.dto.user;

import cz.buca.demo.server.dto.Search;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SearchUser extends Search {

	private String name;
	private String login;
	private String email;
	private Boolean active;
}