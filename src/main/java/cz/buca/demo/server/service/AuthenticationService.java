package cz.buca.demo.server.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cz.buca.demo.server.config.ApplicationConfig;
import cz.buca.demo.server.dto.Login;
import cz.buca.demo.server.dto.Session;
import cz.buca.demo.server.exception.ExpiredTokenException;
import cz.buca.demo.server.exception.ServiceException;
import cz.buca.demo.server.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	public String generateToken(UserPrincipal userPrincipal, boolean isRefreshToken) throws ServiceException {
		String token = null;
		Integer minutesExpiration = null;
		
		try {
			minutesExpiration = applicationConfig.getToken().getExpiration();
			
			if (isRefreshToken) {
				Integer minutesRefresh = applicationConfig.getToken().getRefresh();
				minutesExpiration = minutesExpiration + minutesRefresh;
			}
			
			Date expiration = new Date(System.currentTimeMillis() + minutesExpiration * 60 * 1000);
			String secret = applicationConfig.getToken().getSecret();
			String roles = userPrincipal.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));			
			token = Jwts.builder()
				.setSubject(userPrincipal.getId())
				.claim("id", userPrincipal.getName())
				.claim("name", userPrincipal.getName())
				.claim("roles", roles)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
			
		} catch (Exception exception) {			
			throw new ServiceException("generate token with "+ userPrincipal +", minutesExpiration "+ minutesExpiration +" and isRefreshToken "+ isRefreshToken +" faild", exception);
		}
		
		log.debug("generate token return "+ token +" for "+ userPrincipal +", minutesExpiration "+ minutesExpiration +" and isRefreshToken "+ isRefreshToken);
		
		return token;
	}
	
	public UsernamePasswordAuthenticationToken validateToken(String token) throws ExpiredTokenException {
		UsernamePasswordAuthenticationToken authentication = null;
		
		if (token != null && !token.equals("")) {
			try {
				String secret = applicationConfig.getToken().getSecret();
				Claims claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
				String id = claims.get("id").toString();
				String name = claims.get("name").toString();
				String login = claims.getSubject();				
				Collection<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
				UserPrincipal userPrincipal = new UserPrincipal(id, name, login, authorities);
				authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
				
			} catch (ExpiredJwtException expiredJwtException) {
				throw new ExpiredTokenException("validate token "+ token +" faild, token expired", expiredJwtException);
				
			} catch (Exception exception) {
				log.error("validate token faild", exception);
			}
		}
		
		log.debug("validate token return "+ authentication +" for token "+ token);
		
		return authentication;
	}	
	
	public Session login(Login login) throws ServiceException {
		Session session = null;
		
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			session = new Session();
			String token = generateToken(userPrincipal, false);
			String refresh = generateToken(userPrincipal, true);
			List<String> roles = userPrincipal.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
			
			session.setId(userPrincipal.getId());
			session.setName(userPrincipal.getName());
			session.setToken(token);
			session.setRefresh(refresh);
			session.setRoles(roles);
			
		} catch (Exception exception) {			
			throw new ServiceException("login with "+ login +" faild", exception);
		}

		log.debug("login return " + session + " for " + login);

		return session;
	}
	
	public Session refresh() throws ServiceException {
		return null;
	}
}
