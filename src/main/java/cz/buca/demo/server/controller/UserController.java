package cz.buca.demo.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.buca.demo.server.dto.Data;
import cz.buca.demo.server.dto.user.SearchUser;
import cz.buca.demo.server.dto.user.UserChangePassword;
import cz.buca.demo.server.dto.user.UserCreate;
import cz.buca.demo.server.dto.user.UserDetail;
import cz.buca.demo.server.dto.user.UserSearch;
import cz.buca.demo.server.dto.user.UserUpdate;
import cz.buca.demo.server.exception.ServiceException;
import cz.buca.demo.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDetail> all() throws ServiceException {
		List<UserDetail> details = userService.findAll();

		log.debug("all return " + details);

		return details;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public Data<UserSearch> search(SearchUser search) throws ServiceException {
		Data<UserSearch> data = userService.search(search);

		log.debug("search return " + data + " for " + search);

		return data;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(path = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail get(@PathVariable Long id) throws ServiceException {
		UserDetail detail = userService.findById(id);

		log.debug("get return " + detail + " for ID " + id);

		return detail;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail create(@RequestBody UserCreate create) throws ServiceException {
		UserDetail detail = userService.create(create);

		log.debug("create return " + detail + " for " + create);

		return detail;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail update(@PathVariable Long id, @RequestBody UserUpdate update) throws ServiceException {
		UserDetail detail = userService.update(id, update);

		log.debug("update return " + detail + " for ID " + id + " and " + update);

		return detail;
	}
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PutMapping(path = "/changePassword/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail changePassword(@PathVariable Long id, @RequestBody UserChangePassword changePassword) throws ServiceException {
		UserDetail detail = userService.changePassword(id, changePassword);

		log.debug("change password return " + detail + " for ID " + id + " and " + changePassword);

		return detail;
	}

	@Operation(security = @SecurityRequirement(name = "jwt"))
	@DeleteMapping(path = "/delete/{id}")
	public UserDetail delete(@PathVariable Long id) throws ServiceException {
		UserDetail detail = userService.deleteById(id);
		
		log.debug("delete return "+ detail +" for ID " + id);
	
		return detail;
	}
}