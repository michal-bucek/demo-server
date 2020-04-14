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
import cz.buca.demo.server.dto.user.SearchUser;
import cz.buca.demo.server.dto.user.UserChangePassword;
import cz.buca.demo.server.dto.user.UserCreate;
import cz.buca.demo.server.dto.user.UserDetail;
import cz.buca.demo.server.dto.user.UserSearch;
import cz.buca.demo.server.dto.user.UserUpdate;
import cz.buca.demo.server.entity.UserEntity;
import cz.buca.demo.server.maper.DtoMapper;
import cz.buca.demo.server.repository.UserRepository;
import cz.buca.demo.server.security.UserSession;
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
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private PreferenceService preferenceService;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<UserEntity> optional = userRepository.findByLogin(userName);
		
		if (optional.isEmpty()) {
			UserEntity admin = applicationConfig.getAdmin();
			
			if (userName.equals(admin.getLogin())) {
				log.info("try to login as config admin");
				
				optional = Optional.of(admin);
			}
		}
		
		if (optional.isEmpty()) {
			throw new UsernameNotFoundException("user with login " + userName + " not found");
		}
		
		UserSession userSession = new UserSession(optional.get());
		
		log.debug("loadUserByUsername return "+ userSession +" for userName "+ userName);
		
		return userSession;
	}	

	public Data<UserSearch> search(SearchUser search) {
		Page<UserSearch> page = userRepository.search(search);
		Data<UserSearch> data = new Data<UserSearch>(page);
		
		log.debug("search return "+ data +" for "+ search);
		
		return data;
	}
	
	public List<UserDetail> findAll() {
		List<UserDetail> details = userRepository.findAll()
			.stream()
			.map(entity -> dtoMapper.toUserDetail(entity))
			.collect(Collectors.toList());
		
		log.debug("find all return "+ details);
		
		return details;
	}
 	
	public UserDetail findById(Long id) {
		UserEntity user = userRepository.findById(id).get();
		UserDetail detail = dtoMapper.toUserDetail(user);
		
		log.debug("fing by ID return "+ detail +" for ID "+ id);
		
		return detail;
	}

	public UserDetail create(UserCreate create) {
		UserEntity oldEntity = dtoMapper.toUserEntity(create);
		String encodePass = passwordEncoder.encode(oldEntity.getPass());
		
		oldEntity.setPass(encodePass);
		
		UserEntity newEntity = userRepository.save(oldEntity);
		UserDetail detail = dtoMapper.toUserDetail(newEntity);
		
		eventService.publish("/event/user", "[User] created", detail);		
		log.debug("cretae return "+ detail +" for "+ create);
		
		return detail;
	}

	public UserDetail update(Long id, UserUpdate update) {
		UserEntity oldEntity = userRepository.findById(id).get();
		
		dtoMapper.updateUserEntity(update, oldEntity);
		
		UserEntity newEntity = userRepository.save(oldEntity);
		UserDetail detail = dtoMapper.toUserDetail(newEntity);
		
		eventService.publish("/event/user", "[User] updated", detail);	
		log.debug("update return "+ detail +" for ID "+ id +" and "+ update);
		
		return detail;
	}
	
	public UserDetail changePassword(Long id, UserChangePassword changePassword) {
		UserEntity oldEntity = userRepository.findById(id).get();
		String encodePass = passwordEncoder.encode(changePassword.getPass());
		
		oldEntity.setPass(encodePass);
		
		UserEntity newEntity = userRepository.save(oldEntity);
		UserDetail detail = dtoMapper.toUserDetail(newEntity);
		
		eventService.publish("/event/user", "[User] change password", detail);	
		log.debug("change password return "+ detail +" for ID "+ id +" and "+ changePassword);
		
		return detail;
	}
	
	public UserDetail deleteById(Long id) {		
		UserEntity user = userRepository.findById(id).get();
		UserDetail detail = dtoMapper.toUserDetail(user);
		
		preferenceService.deleteByParentId(id);
		userRepository.delete(user);
		eventService.publish("/event/user", "[User] deleted", detail);		
		log.info("delete by ID return "+ detail +" for ID "+ id);
		
		return detail;
	}
}