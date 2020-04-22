package cz.buca.demo.server.maper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import cz.buca.demo.server.entity.PreferenceEntity;
import cz.buca.demo.server.entity.UserEntity;
import cz.buca.demo.server.model.preference.PreferenceDetail;
import cz.buca.demo.server.model.preference.PreferenceSave;
import cz.buca.demo.server.model.user.UserCreate;
import cz.buca.demo.server.model.user.UserDetail;
import cz.buca.demo.server.model.user.UserUpdate;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ModelMapper {
	
	public abstract UserDetail toUserDetail(UserEntity entity);
	
	public abstract UserEntity toUserEntity(UserDetail detail);
	
	public abstract UserEntity toUserEntity(UserCreate create);
	
	public abstract void updateUserEntity(UserUpdate update, @MappingTarget UserEntity entity);
	
	public abstract PreferenceDetail toPreferenceDetail(PreferenceEntity entity);
	
	public abstract PreferenceEntity toPreferenceEntity(PreferenceSave save);
	
	public abstract void updatePreferenceEntity(PreferenceSave save, @MappingTarget PreferenceEntity entity);
}