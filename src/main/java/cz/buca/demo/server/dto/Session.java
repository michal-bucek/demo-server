package cz.buca.demo.server.dto;

import java.util.List;

import cz.buca.demo.server.dto.preference.PreferenceDetail;
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