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
import cz.buca.demo.server.dto.Search;
import cz.buca.demo.server.dto.UserCreate;
import cz.buca.demo.server.dto.UserDetail;
import cz.buca.demo.server.dto.UserSearch;
import cz.buca.demo.server.dto.UserUpdate;
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
		List<UserDetail> users = userService.findAll();
		
		log.debug("search return "+ users);
		
		return users;
	}
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public Data<UserSearch> search(Search search) throws ServiceException {
		Data<UserSearch> data = userService.search(search);
		
		log.debug("search return "+ data +" for "+ search);
		
		return data;
	}
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@GetMapping(path = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail get(@PathVariable Long id) throws ServiceException {
		UserDetail user = userService.findById(id);
		
		log.debug("read return "+ user +" for ID "+ id);
		
		return user;
	}
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail create(@RequestBody UserCreate userCreate) throws ServiceException {
		UserDetail userDetail = userService.create(userCreate);
		
		log.debug("create return "+ userDetail +" for "+ userCreate);
		
		return userDetail;			
	}
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDetail update(@PathVariable Long id, @RequestBody UserUpdate userUpdate) throws ServiceException {
		UserDetail userDetail = userService.update(id, userUpdate);
		
		log.debug("update return "+ userDetail +" for "+ userUpdate);
		
		return userDetail;		
	}
	
	@Operation(security = @SecurityRequirement(name = "jwt"))
	@DeleteMapping(path = "/delete/{id}")
	public void delete(@PathVariable Long id) throws ServiceException {
		log.debug("delete with ID "+ id);
		userService.deleteById(id);
	}
}