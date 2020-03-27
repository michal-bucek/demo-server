package cz.buca.demo.server.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ServiceException {

	private static final long serialVersionUID = 3433688446241309196L;

	public ExpiredTokenException() {
		super();
	}

	public ExpiredTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExpiredTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExpiredTokenException(String message) {
		super(message);
	}

	public ExpiredTokenException(Throwable cause) {
		super(cause);
	}
	
	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.FORBIDDEN;
	}
}