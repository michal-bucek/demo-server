package cz.buca.demo.server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cz.buca.demo.server.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherException(Exception exception, WebRequest request) {
		log.error("handle other exception", exception);
		
		return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException serviceException, WebRequest request) {
		log.error("handle service exception", serviceException);
		
		return handleExceptionInternal(serviceException, serviceException.getMessage(), new HttpHeaders(), serviceException.getHttpStatus(), request);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException badCredentialsException, WebRequest request) {
		log.error("handle bad credentials exception", badCredentialsException);
		
		return handleExceptionInternal(badCredentialsException, badCredentialsException.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}


}
