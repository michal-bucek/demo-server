package cz.buca.demo.server.maper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import cz.buca.demo.server.dto.user.UserCreate;
import cz.buca.demo.server.dto.user.UserDetail;
import cz.buca.demo.server.dto.user.UserUpdate;
import cz.buca.demo.server.entity.User;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DtoMapper {
	
	public abstract UserDetail toUserDetail(User user);
	
	public abstract User toUser(UserDetail detail);
	
	public abstract User toUser(UserCreate create);
	
	public abstract void updateUser(UserUpdate update, @MappingTarget User user);
}