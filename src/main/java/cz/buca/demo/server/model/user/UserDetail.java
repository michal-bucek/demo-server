package cz.buca.demo.server.model.user;

import java.util.Date;
import java.util.List;

import cz.buca.demo.server.model.preference.PreferenceDetail;
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
	private Boolean active;
	private List<String> roles;
	private List<PreferenceDetail> preferences;
	
	private String creator;
	private Date created;
	private String modifier;
	private Date modified;
}