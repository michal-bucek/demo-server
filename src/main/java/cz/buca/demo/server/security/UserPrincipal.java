package cz.buca.demo.server.security;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cz.buca.demo.server.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class UserPrincipal implements UserDetails {
	
	private static final long serialVersionUID = 7647627706949417160L;

	private String id;
	private String name;
	private String password;
	private String username;
	private Collection<GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	
	public UserPrincipal(User user) {
		this.id = UUID.randomUUID().toString();
		this.name = user.getName();
		this.password = user.getPass();
		this.username = user.getLogin();
		this.authorities = user.getRoles()
			.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}
	
	public UserPrincipal(String id, String name, String username, Collection<GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.authorities = authorities;
	}
}