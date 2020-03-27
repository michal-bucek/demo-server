package cz.buca.demo.server.maper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import cz.buca.demo.server.dto.UserCreate;
import cz.buca.demo.server.dto.UserDetail;
import cz.buca.demo.server.dto.UserUpdate;
import cz.buca.demo.server.entity.User;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DtoMapper {
	
	public abstract UserDetail toUserDetail(User user);
	
	public abstract User toUser(UserDetail userDetail);
	
	public abstract User toUser(UserCreate userCreate);
	
	public abstract void updateUser(UserUpdate userUpdate, @MappingTarget User user);
}