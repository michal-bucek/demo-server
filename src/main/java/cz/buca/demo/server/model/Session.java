package cz.buca.demo.server.model;

import java.util.List;

import cz.buca.demo.server.model.preference.PreferenceDetail;
import lombok.Data;

@Data
public class Session {

	private String uuid;
	private Long id;
	private String name;
	private String token;
	private String refresh;
	private List<String> roles;
	private List<PreferenceDetail> preferences;
}