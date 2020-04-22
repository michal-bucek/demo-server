package cz.buca.demo.server.model.preference;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDetail {

	private Long id;
	private String name;
	private String value;
	private Long parentId;
	
	private String creator;
	private Date created;
	private String modifier;
	private Date modified;
}