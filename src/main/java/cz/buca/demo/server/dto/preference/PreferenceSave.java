package cz.buca.demo.server.dto.preference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceSave {

	private Long id;
	private String name;
	private String value;
	private Long parentId;
}