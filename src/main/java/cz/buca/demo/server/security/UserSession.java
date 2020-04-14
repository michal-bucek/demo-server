package cz.buca.demo.server.security;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cz.buca.demo.server.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserSession implements UserDetails {
	
	private static final long serialVersionUID = 7647627706949417160L;

	private String uuid;
	private Long id;
	private String name;
	private String password;
	private String username;
	private Collection<GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	
	public UserSession(UserEntity userEntity) {
		this.uuid = UUID.randomUUID().toString();
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.password = userEntity.getPass();
		this.username = userEntity.getLogin();
		this.authorities = userEntity.getRoles()
			.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
		this.enabled = userEntity.getActive();
	}
	
	public UserSession(String uuid, Long id, String name, String username, Collection<GrantedAuthority> authorities) {
		this.uuid = uuid;
		this.id = id;
		this.name = name;
		this.username = username;
		this.authorities = authorities;
	}
}