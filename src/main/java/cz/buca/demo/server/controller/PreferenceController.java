package cz.buca.demo.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.buca.demo.server.dto.preference.PreferenceDetail;
import cz.buca.demo.server.dto.preference.PreferenceSave;
import cz.buca.demo.server.exception.ServiceException;
import cz.buca.demo.server.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pref")
@Slf4j
public class PreferenceController {

	@Autowired
	private PreferenceService preferenceService;	
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PreferenceDetail save(@RequestBody PreferenceSave save) throws ServiceException {
		PreferenceDetail detail = preferenceService.save(save);
		
		log.debug("save preference return "+ detail +" for "+ save);
		
		return detail;	
	}
}