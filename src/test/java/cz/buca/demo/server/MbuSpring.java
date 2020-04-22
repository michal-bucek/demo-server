package cz.buca.demo.server;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cz.buca.demo.server.model.user.UserChangePassword;
import cz.buca.demo.server.model.user.UserCreate;
import cz.buca.demo.server.model.user.UserDetail;
import cz.buca.demo.server.service.UserService;

@SpringBootTest
public class MbuSpring {

	@Autowired
	private UserService userService;
	
	@Test
	public void test() {
		UserCreate create = new UserCreate("MBU test", "mbu_test", "mbu_test", "mbu_test", true, Arrays.asList("USER"));
		UserDetail detail = userService.create(create);
		Long id = detail.getId();
		
		try {
			userService.changePassword(id, new UserChangePassword("mbu_test_new"));
			
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			
		} finally {
			userService.deleteById(id);
		}	
	}
}