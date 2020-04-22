package cz.buca.demo.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.buca.demo.server.exception.ServiceException;
import cz.buca.demo.server.model.Login;
import cz.buca.demo.server.model.Session;
import cz.buca.demo.server.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rest/auth")
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Session login(@RequestBody Login login) throws ServiceException {
		Session session = authenticationService.login(login);

		log.debug("login return " + session + " for " + login);

		return session;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public Session refresh() throws ServiceException {
		Session session = authenticationService.refresh();

		log.debug("refresh return " + session);

		return session;
	}
}