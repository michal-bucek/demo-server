package cz.buca.demo.server.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import cz.buca.demo.server.config.ApplicationConfig;
import cz.buca.demo.server.exception.ExpiredTokenException;
import cz.buca.demo.server.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private ApplicationConfig applicationConfig;	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestTokenHeader = request.getHeader("Authorization");
		
		log.debug("doFilterInternal with requestTokenHeader "+ requestTokenHeader);		
		response.addHeader("Access-Control-Allow-Headers","Access-Control-Allow-Origin, Origin, Accept, X-Requested-With, Authorization, refreshauthorization, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Credentials");
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			String origin = applicationConfig.getCors().getOrigin();
			
			response.addHeader("Access-Control-Allow-Origin", origin);
		}
		
		if (response.getHeader("Access-Control-Allow-Credentials") == null) {
			response.addHeader("Access-Control-Allow-Credentials", "true");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
		}
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			String token = requestTokenHeader.substring(7);
			
			try {
				UsernamePasswordAuthenticationToken authentication = authenticationService.validateToken(token);
				
				if (authentication != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					logger.info("setting security context with "+ authentication);
				}
				
			} catch (ExpiredTokenException expiredTokenException) {
				response.setStatus(expiredTokenException.getHttpStatus().value());
				
				return;
			}
		}
		
		filterChain.doFilter(request, response);
	}
}