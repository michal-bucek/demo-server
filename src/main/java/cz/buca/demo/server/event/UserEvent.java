package cz.buca.demo.server.event;

import cz.buca.demo.server.dto.user.UserDetail;

public class UserEvent extends Event {
	
	public UserEvent(String type, Object data) {
		super(type, data);
	}

	public static UserEvent updated(UserDetail detail) {
		return new UserEvent("[User] updated", detail);
	}
	
	public static UserEvent created(UserDetail detail) {
		return new UserEvent("[User] created", detail);
	}
	
	public static UserEvent deleted(UserDetail detail) {
		return new UserEvent("[User] deleted", detail);
	}
}