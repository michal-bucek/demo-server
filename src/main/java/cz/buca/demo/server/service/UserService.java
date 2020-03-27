package cz.buca.demo.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.config.ApplicationConfig;
import cz.buca.demo.server.dto.Data;
import cz.buca.demo.server.dto.Search;
import cz.buca.demo.server.dto.UserCreate;
import cz.buca.demo.server.dto.UserDetail;
import cz.buca.demo.server.dto.UserSearch;
import cz.buca.demo.server.dto.UserUpdate;
import cz.buca.demo.server.entity.User;
import cz.buca.demo.server.maper.DtoMapper;
import cz.buca.demo.server.repository.UserRepository;
import cz.buca.demo.server.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DtoMapper dtoMapper;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> optional = userRepository.findByLogin(userName);
		
		if (optional.isEmpty()) {
			User admin = applicationConfig.getAdmin();
			
			if (userName.equals(admin.getLogin())) {
				log.info("try to login as config admin");
				
				optional = Optional.of(admin);
			}
		}
		
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException("user with login " + userName + " not found");
		}
		
		UserPrincipal userPrincipal = new UserPrincipal(optional.get());
		
		log.debug("loadUserByUsername return "+ userPrincipal +" for userName "+ userName);
		
		return userPrincipal;
	}	

	public Data<UserSearch> search(Search search) {
		Page<UserSearch> page = userRepository.search(search);
		Data<UserSearch> data = new Data<UserSearch>(page);
		
		log.debug("search return "+ data +" for "+ search);
		
		return data;
	}
	
	public List<UserDetail> findAll() {
		List<UserDetail> userDetails = userRepository.findAll()
			.stream()
			.map(user -> dtoMapper.toUserDetail(user))
			.collect(Collectors.toList());
		
		log.debug("find all return "+ userDetails);
		
		return userDetails;
	}
 	
	public UserDetail findById(Long id) {
		User user = userRepository.findById(id).get();
		UserDetail userDetail = dtoMapper.toUserDetail(user);
		
		log.debug("fing by ID return "+ userDetail +" for ID "+ id);
		
		return userDetail;
	}

	public UserDetail create(UserCreate userCreate) {
		User oldUser = dtoMapper.toUser(userCreate);
		String encodePass = passwordEncoder.encode(oldUser.getPass());
		
		oldUser.setPass(encodePass);
		
		User newUser = userRepository.save(oldUser);
		UserDetail userDetail = dtoMapper.toUserDetail(newUser);
		
		log.debug("cretae return "+ userDetail +" for "+ userCreate);
		
		return userDetail;
	}

	public UserDetail update(Long id, UserUpdate userUpdate) {
		User oldUser = userRepository.findById(id).get();
		
		dtoMapper.updateUser(userUpdate, oldUser);
		
		User newUser = userRepository.save(oldUser);
		UserDetail userDetail = dtoMapper.toUserDetail(newUser);
		
		log.debug("update return "+ userDetail +" for ID "+ id +" and "+ userUpdate);
		
		return userDetail;
	}
	
	public void deleteById(Long id) {
		log.info("delete by ID with ID "+ id);
		userRepository.deleteById(id);
	}
}