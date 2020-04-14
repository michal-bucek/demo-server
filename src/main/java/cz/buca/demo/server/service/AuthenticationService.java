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
import cz.buca.demo.server.dto.preference.PreferenceDetail;
import cz.buca.demo.server.exception.ExpiredTokenException;
import cz.buca.demo.server.exception.ServiceException;
import cz.buca.demo.server.security.UserSession;
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
	
	@Autowired
	private PreferenceService preferenceService;
	
	private Session createSession(UserSession userSession) throws ServiceException {
		Session session = new Session();
		String token = generateToken(userSession, false);
		String refresh = generateToken(userSession, true);
		List<String> roles = userSession.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());
		Long id = userSession.getId();
		List<PreferenceDetail> preferences = preferenceService.findByParentId(id);
		
		session.setUuid(userSession.getUuid());
		session.setId(id);
		session.setName(userSession.getName());
		session.setToken(token);
		session.setRefresh(refresh);
		session.setRoles(roles);
		session.setPreferences(preferences);
		
		log.debug("create session return "+ session +" for "+ userSession);
		
		return session;
	}
	
	public String generateToken(UserSession userSession, boolean isRefreshToken) throws ServiceException {
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
			String roles = userSession.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));			
			token = Jwts.builder()
				.setSubject(userSession.getUsername())
				.claim("uuid", userSession.getUuid())
				.claim("id", userSession.getId())
				.claim("name", userSession.getName())
				.claim("roles", roles)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
			
		} catch (Exception exception) {			
			throw new ServiceException("generate token with "+ userSession +", minutesExpiration "+ minutesExpiration +" and isRefreshToken "+ isRefreshToken +" faild", exception);
		}
		
		log.debug("generate token return "+ token +" for "+ userSession +", minutesExpiration "+ minutesExpiration +" and isRefreshToken "+ isRefreshToken);
		
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
				String uuid = claims.get("uuid").toString();
				Long id = Long.valueOf(claims.get("id").toString());
				String name = claims.get("name").toString();
				String login = claims.getSubject();				
				Collection<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
				UserSession userSession = new UserSession(uuid, id, name, login, authorities);
				authentication = new UsernamePasswordAuthenticationToken(userSession, null, authorities);
				
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
			UserSession userSession = (UserSession) authentication.getPrincipal();
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			session = createSession(userSession);
			
		} catch (Exception exception) {			
			throw new ServiceException("login with "+ login +" faild", exception);
		}

		log.debug("login return " + session + " for " + login);

		return session;
	}
	
	public Session refresh() throws ServiceException {
		Session session = null;
		
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserSession userSession = (UserSession) authentication.getPrincipal();			
			session = createSession(userSession);
			
		} catch (Exception exception) {			
			throw new ServiceException("refresh faild", exception);
		}

		log.debug("refresh return " + session);

		return session;
	}
}
